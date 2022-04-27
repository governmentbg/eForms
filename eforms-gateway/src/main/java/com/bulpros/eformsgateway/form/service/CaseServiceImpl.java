package com.bulpros.eformsgateway.form.service;

import com.bulpros.eformsgateway.form.web.controller.dto.AdditionalProfileRoleEnum;
import com.bulpros.eformsgateway.form.web.controller.dto.AdminCaseFilter;
import com.bulpros.eformsgateway.form.web.controller.dto.CaseFilter;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.exception.ResourceNotFoundException;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.SubmissionFilterClauseEnum;
import com.bulpros.formio.security.FormioUserService;
import com.bulpros.formio.service.SubmissionService;
import com.bulpros.formio.utils.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaseServiceImpl implements CaseService {
    private static final String HYPHEN_SYMBOL = "-";
    private static final String DATA_STATUS_NAME = "data.statusName";
    public static final String CODE_KEY = "code";
    public static final String STATUS_NAME_KEY = "statusName";
    public static final String STAGE_NAME_KEY = "stageName";
    public static final String CHANNEL_NAME_KEY = "channelName";
    public static final String SERVICE_ID_KEY = "serviceId";
    public static final String AR_ID_KEY = "arId";
    public static final String STATUS_CODE_KEY = "statusCode";
    public static final String STAGE_KEY = "stage";
    public static final String CHANNEL_KEY = "channelType";
    public static final String VALUE_KEY = "value";
    public static final String REQUESTOR_FULL_USER_NAME_KEY = "requestorFullUserName";
    public static final String EXIST_SERVICE = "existService";

    private final FormioUserService userService;
    private final UserProfileService userProfileService;
    private final ConfigurationProperties configuration;
    private final SubmissionService submissionService;

    @Override
    public Page<ResourceDto> getAdminCasesByUserIdAndStatusClassifier(String projectId, Authentication authentication, String classifier,
                                                                      @Valid AdminCaseFilter caseFilter, Long page, Long size, String sort) {
        List<SubmissionFilter> filters = getAdminFilters(caseFilter);
        return getCasePage(projectId, authentication, classifier, page, size, sort, filters);
    }

    @Override
    public ResourceDto getAdminCaseByUserIdAndCaseBusinessKey(String projectId, Authentication authentication, String caseBusinessKey,
                                                     @Valid AdminCaseFilter caseFilter) {
        List<SubmissionFilter> filters = getAdminFilters(caseFilter);
        return getCase(projectId, authentication, caseBusinessKey, filters);
    }

    @Override
    public boolean existsAdminCaseByUserIdAndCaseBusinessKey(String projectId, Authentication authentication, String caseBusinessKey,
                                                             @Valid AdminCaseFilter caseFilter) {
        List<SubmissionFilter> filters = getAdminFilters(caseFilter);
        return existByUserIdAndCaseBusinessKey(projectId, authentication, caseBusinessKey, filters);
    }

    private ResourceDto getCase(String projectId, Authentication authentication, String caseBusinessKey, List<SubmissionFilter> filters) {
        filters.add(new SubmissionFilter(
                SubmissionFilterClauseEnum.IN,
                Collections.singletonMap(configuration.getBusinessKey(), caseBusinessKey)));

        var caseSubmissions = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getCaseResourcePath()), authentication, filters);

        if (caseSubmissions == null || caseSubmissions.isEmpty()) {
            log.error("No case with business key = {} is found.", caseBusinessKey);
            throw new ResourceNotFoundException("No case with business key = " + caseBusinessKey + " is found.");
        }
        var caseSubmission = caseSubmissions.stream().findFirst().get();

        var serviceSubmissions = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getServiceResourcePath()), authentication,
                Collections.singletonList(
                        new SubmissionFilter(
                                SubmissionFilterClauseEnum.IN,
                                Collections.singletonMap(configuration.getArId(), caseSubmission.getData().get(SERVICE_ID_KEY).toString()))));

        var caseStatusIdToNameMap = getStatusIdToNameMapByCases(projectId, authentication, Collections.singletonList(caseSubmission));
        var caseStageIdToNameMap = getStageIdToNameMapByCases(projectId, authentication, Collections.singletonList(caseSubmission));
        var channelTypeToNameMap = getChannelTypeToNameMapByCases(projectId, authentication, Collections.singletonList(caseSubmission));

        var userId = (String) caseSubmission.getData().get(configuration.getRequestorPropertyKey());
        var userIdToUserNameMap = userProfileService.getUserIdToUserNameMap(projectId, authentication, Collections.singletonList(userId));

        addCaseExternalProperties(Arrays.asList(caseSubmission), serviceSubmissions, caseStatusIdToNameMap,
                caseStageIdToNameMap, channelTypeToNameMap, userIdToUserNameMap);

        return caseSubmission;
    }

    @Override
    public Page<ResourceDto> getCasesByUserIdAndStatusClassifier(String projectId, Authentication authentication, String classifier,
                                                                 CaseFilter caseFilter, Long page, Long size, String sort) {

        List<SubmissionFilter> filters = getFilters(caseFilter, projectId, authentication);
        return getCasePage(projectId, authentication, classifier, page, size, sort, filters);
    }

    @Override
    public ResourceDto getCaseByUserIdAndCaseBusinessKey(String projectId, Authentication authentication, String caseBusinessKey, CaseFilter caseFilter) {
        List<SubmissionFilter> filters = getFilters(caseFilter, projectId, authentication);
        return getCase(projectId, authentication, caseBusinessKey, filters);
    }

    @Override
    public boolean existsCaseByUserIdAndCaseBusinessKey(String projectId, Authentication authentication, String caseBusinessKey,
                                                        CaseFilter caseFilter) {
        List<SubmissionFilter> filters = getFilters(caseFilter, projectId, authentication);
        return existByUserIdAndCaseBusinessKey(projectId, authentication, caseBusinessKey, filters);
    }

    private boolean existByUserIdAndCaseBusinessKey(String projectId, Authentication authentication, String caseBusinessKey,
                                                    List<SubmissionFilter> filters) {
        filters.add(new SubmissionFilter(
                SubmissionFilterClauseEnum.NONE,
                Collections.singletonMap(configuration.getBusinessKey(), caseBusinessKey)));

        var caseSubmissions = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getCaseResourcePath()), authentication, filters);

        return caseSubmissions != null && !caseSubmissions.isEmpty();
    }

    @Override
    public List<ResourceDto> getIncompleteCasesByUserIdAndServiceIds(String projectId, Authentication authentication, List<String> serviceIds) {
        var user = userService.getUser(authentication);

        var caseStatuses = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getCaseStatusResourcePath()), authentication,
                Collections.singletonList(
                        new SubmissionFilter(
                                SubmissionFilterClauseEnum.IN,
                                Collections.singletonMap(configuration.getCaseStatusClassifierPropertyKey(),
                                        Collections.singletonList(CaseStatusClassifierEnum.SERVICE_IN_COMPLETION.classifier)))));

        var caseStatusCodes = caseStatuses
                .stream()
                .map(s -> (String) s.getData().get(CODE_KEY))
                .collect(Collectors.toList());
        List<SubmissionFilter> filters = new ArrayList<>();
        filters.add(new SubmissionFilter(
                SubmissionFilterClauseEnum.IN,
                Collections.singletonMap(configuration.getRequestorPropertyKey(), user.getPersonIdentifier())));
        filters.add(new SubmissionFilter(
                SubmissionFilterClauseEnum.IN,
                Collections.singletonMap(configuration.getServiceIdPropertyKey(), serviceIds)));
        filters.add(new SubmissionFilter(
                SubmissionFilterClauseEnum.NIN,
                Collections.singletonMap(configuration.getCaseStatusCodePropertyKey(), caseStatusCodes)));
        var caseSubmissions = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getCaseResourcePath()), authentication, filters);

        if (caseSubmissions == null || caseSubmissions.isEmpty()) {
            return Arrays.asList();
        }

        var serviceSubmissions = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getServiceResourcePath()), authentication,
                Collections.singletonList(
                        new SubmissionFilter(
                                SubmissionFilterClauseEnum.IN,
                                Collections.singletonMap(configuration.getServiceResourcePath(), serviceIds))));

        var caseStatusIdToNameMap = getStatusIdToNameMapByCases(projectId, authentication, caseSubmissions);
        var caseStageIdToNameMap = getStageIdToNameMapByCases(projectId, authentication, caseSubmissions);

        var userIds = caseSubmissions
                .stream()
                .map(r -> (String) r.getData().get(configuration.getRequestorPropertyKey()))
                .collect(Collectors.toList());
        var userIdToUserNameMap = userProfileService.getUserIdToUserNameMap(projectId, authentication, userIds);
        addCaseExternalProperties(caseSubmissions, serviceSubmissions, caseStatusIdToNameMap,
                caseStageIdToNameMap, null, userIdToUserNameMap);

        return caseSubmissions;
    }

    private void sortCasesByExternalProperties(List<ResourceDto> customCaseSubmissions, String sort) {
        if (sort == null || sort.isEmpty()) {
            return;
        }

        switch (sort) {
            case DATA_STATUS_NAME:
                customCaseSubmissions
                        .sort((c1, c2) -> ((String) c1.getData().get(STATUS_NAME_KEY)).compareTo((String) c2.getData().get(STATUS_NAME_KEY)));
                break;
            case HYPHEN_SYMBOL + DATA_STATUS_NAME:
                customCaseSubmissions
                        .sort((c1, c2) -> ((String) c2.getData().get(STATUS_NAME_KEY)).compareTo((String) c1.getData().get(STATUS_NAME_KEY)));
                break;
        }
    }

    private Map<String, String> getStageIdToNameMapByCases(String projectId, Authentication authentication, List<ResourceDto> cases) {
        var stageIds = cases
                .stream()
                .map(c -> c.getData().get(STAGE_KEY).toString())
                .distinct()
                .collect(Collectors.toList());

        var stageSubmissions = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getCaseStageResourcePath()), authentication,
                Collections.singletonList(
                        new SubmissionFilter(
                                SubmissionFilterClauseEnum.IN,
                                Collections.singletonMap(configuration.getCodePropertyKey(), stageIds))));

        var stageIdToNameMap = stageSubmissions
                .stream()
                .map(status -> status.getData())
                .collect(Collectors.toMap(statusData -> (String) statusData.get(CODE_KEY), statusData -> (String) statusData.get(VALUE_KEY)));
        return stageIdToNameMap;
    }

    private Map<String, String> getStatusIdToNameMapByCases(String projectId, Authentication authentication, List<ResourceDto> cases) {
        var statusIds = cases
                .stream()
                .map(c -> c.getData().get(STATUS_CODE_KEY).toString())
                .collect(Collectors.toList());


        var statusSubmissions = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getCaseStatusResourcePath()), authentication,
                Collections.singletonList(
                        new SubmissionFilter(
                                SubmissionFilterClauseEnum.IN,
                                Collections.singletonMap(configuration.getCodePropertyKey(), statusIds))));

        var statusIdToNameMap = statusSubmissions
                .stream()
                .map(status -> status.getData())
                .collect(Collectors.toMap(statusData -> (String) statusData.get(CODE_KEY), statusData -> (String) statusData.get(VALUE_KEY)));
        return statusIdToNameMap;
    }

    private Map<String, String> getChannelTypeToNameMapByCases(String projectId, Authentication authentication, List<ResourceDto> cases) {
        var channelTypes = cases
                .stream()
                .filter(c -> c.getData().get(CHANNEL_KEY) != null)
                .map(c -> c.getData().get(CHANNEL_KEY).toString())
                .distinct()
                .collect(Collectors.toList());

        var channelSubmissions = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getCaseChannelResourcePath()), authentication,
                Collections.singletonList(
                        new SubmissionFilter(
                                SubmissionFilterClauseEnum.IN,
                                Collections.singletonMap(configuration.getChannelTypePropertyKey(), channelTypes))));

        var channelTypeToNameMap = channelSubmissions
                .stream()
                .map(channel -> channel.getData())
                .collect(Collectors.toMap(channelData -> (String) channelData.get(CHANNEL_KEY), channelData -> (String) channelData.get(CHANNEL_NAME_KEY)));
        return channelTypeToNameMap;
    }

    private Map<String, String> getStatusIdToNameMapByCasesStatuses(List<ResourceDto> caseStatusSubmissions) {
        var caseStatusIdToNameMap = caseStatusSubmissions
                .stream()
                .map(caseStatus -> caseStatus.getData())
                .collect(Collectors.toMap(statusData -> (String) statusData.get(CODE_KEY), statusData -> (String) statusData.get(VALUE_KEY)));
        return caseStatusIdToNameMap;
    }

    private void addCaseExternalProperties(List<ResourceDto> customCaseSubmissions,
                                           List<ResourceDto> serviceSubmissions,
                                           Map<String, String> caseStatusIdToNameMap,
                                           Map<String, String> caseStageIdToNameMap,
                                           Map<String, String> channelTypeToNameMap,
                                           Map<String, String> userIdToUserNameMap) {
        customCaseSubmissions
                .forEach(r -> r.getData().put(EXIST_SERVICE, existService(serviceSubmissions, r.getData().get(SERVICE_ID_KEY).toString())));
        customCaseSubmissions
                .forEach(r -> r.getData().put(STATUS_NAME_KEY, caseStatusIdToNameMap.get(r.getData().get(STATUS_CODE_KEY))));
        customCaseSubmissions
                .forEach(r -> r.getData().put(STAGE_NAME_KEY, caseStageIdToNameMap.get(r.getData().get(STAGE_KEY))));
        if (channelTypeToNameMap != null && !channelTypeToNameMap.isEmpty()) {
            customCaseSubmissions
                    .forEach(r -> r.getData().put(CHANNEL_NAME_KEY, channelTypeToNameMap.get(r.getData().get(CHANNEL_KEY))));
        }
        customCaseSubmissions
                .forEach(r -> r.getData().put(REQUESTOR_FULL_USER_NAME_KEY, userIdToUserNameMap.get(r.getData().get(configuration.getRequestorPropertyKey()))));
    }

    private boolean existService(List<ResourceDto> services, String id) {
        return services.stream().anyMatch(r -> r.getData().get(AR_ID_KEY).toString().equals(id));
    }

    private List<SubmissionFilter> getFilters(CaseFilter caseFilter, String projectId, Authentication authentication) {
        List<SubmissionFilter> filters = new ArrayList<>();

        if (caseFilter.getServiceName() != null && !caseFilter.getServiceName().isEmpty()) {
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.REGEX,
                    Collections.singletonMap(configuration.getServiceName(), "(.*)" + caseFilter.getServiceName() + "(.*)")));
        }

        if (caseFilter.getServiceUri() != null && !caseFilter.getServiceUri().isEmpty()) {
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.REGEX,
                    Collections.singletonMap(configuration.getServiceIdPropertyKey(), "(.*)" + caseFilter.getServiceUri() + "(.*)")));
        }

        if (caseFilter.getBusinessKey() != null && !caseFilter.getBusinessKey().isEmpty()) {
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.REGEX,
                    Collections.singletonMap(configuration.getBusinessKey(), "(.*)" + caseFilter.getBusinessKey() + "(.*)")));
        }

        var userProfile = userProfileService.getUserProfileData(projectId, authentication);
        if (userProfileService.hasUserRole(userProfile, caseFilter.getApplicant(), AdditionalProfileRoleEnum.Admin) ||
            userProfileService.hasUserRole(userProfile, caseFilter.getApplicant(), AdditionalProfileRoleEnum.User)) {

            if (caseFilter.getApplicant() != null && !caseFilter.getApplicant().isEmpty()) {
                filters.add(new SubmissionFilter(
                        SubmissionFilterClauseEnum.NONE,
                        Collections.singletonMap(configuration.getApplicantPropertyKey(), caseFilter.getApplicant())));
            }
        } else if (caseFilter.getRequestor() != null && !caseFilter.getRequestor().isEmpty() &&
                (caseFilter.getApplicant() == null || caseFilter.getApplicant().isEmpty())) {
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.NONE,
                    Collections.singletonMap(configuration.getRequestorPropertyKey(), caseFilter.getRequestor())));
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.EXISTS,
                    Collections.singletonMap(configuration.getApplicantPropertyKey(), false)));
        }

        return filters;
    }

    private List<SubmissionFilter> getAdminFilters(AdminCaseFilter caseFilter) {
        List<SubmissionFilter> filters = new ArrayList<>();
            if (caseFilter.getServiceId() != null && !caseFilter.getServiceId().isEmpty()) {
                filters.add(new SubmissionFilter(
                        SubmissionFilterClauseEnum.NONE,
                        Collections.singletonMap(configuration.getServiceIdPropertyKey(),
                                Collections.singletonList(caseFilter.getServiceId()))));

            }

            if (caseFilter.getBusinessKey() != null && !caseFilter.getBusinessKey().isEmpty()) {
                filters.add(new SubmissionFilter(
                        SubmissionFilterClauseEnum.REGEX,
                        Collections.singletonMap(configuration.getBusinessKey(), "(.*)" + caseFilter.getBusinessKey() + "(.*)")));
            }

            if (caseFilter.getProcessInstanceIds() != null && !caseFilter.getProcessInstanceIds().isEmpty()) {
                filters.add(new SubmissionFilter(
                        SubmissionFilterClauseEnum.IN,
                        Collections.singletonMap(configuration.getProcessInstanceId(), caseFilter.getProcessInstanceIds())));

            }

            if (caseFilter.getFromIssueDate() != null && !caseFilter.getFromIssueDate().isEmpty()) {
                filters.add(new SubmissionFilter(
                        SubmissionFilterClauseEnum.GTE,
                        Collections.singletonMap(configuration.getIssueDate(), caseFilter.getFromIssueDate())));
            }

            if (caseFilter.getToIssueDate() != null && !caseFilter.getToIssueDate().isEmpty()) {
                filters.add(new SubmissionFilter(
                        SubmissionFilterClauseEnum.LTE,
                        Collections.singletonMap(configuration.getIssueDate(), caseFilter.getToIssueDate())));
            }

            if (caseFilter.getUserIds() != null && !caseFilter.getUserIds().isEmpty()) {
                filters.add(new SubmissionFilter(
                        SubmissionFilterClauseEnum.IN,
                        Collections.singletonMap(configuration.getRequestorPropertyKey(), caseFilter.getUserIds())));
            }

            if (caseFilter.getServiceSupplierId() != null && !caseFilter.getServiceSupplierId().isEmpty()) {
                filters.add(new SubmissionFilter(
                        SubmissionFilterClauseEnum.IN,
                        Collections.singletonMap(configuration.getServiceSupplierIdPropertyKey(), caseFilter.getServiceSupplierId())));
            }
        return filters;
    }

    private Page<ResourceDto> getCasePage(String projectId, Authentication authentication, String classifier, Long page, Long size, String sort, List<SubmissionFilter> filters) {
        var caseStatuses = getCaseStatuses(projectId, authentication, classifier);
        var caseStatusCodes = getCaseStatusCodes(caseStatuses);
        filters.add(new SubmissionFilter(
                SubmissionFilterClauseEnum.IN,
                Collections.singletonMap(configuration.getCaseStatusCodePropertyKey(), caseStatusCodes)));

        var paginatedCaseSubmissions = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getCaseResourcePath()), authentication,
                filters, page, size, sort);

        if (paginatedCaseSubmissions.getElements().size() == 0) {
            return new Page<>(0l, 0l, Arrays.asList());
        }

        var serviceIds = paginatedCaseSubmissions.getElements()
                .stream()
                .map(c -> c.getData().get(SERVICE_ID_KEY).toString())
                .distinct()
                .collect(Collectors.toList());

        var serviceSubmissions = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getServiceResourcePath()), authentication,
                Collections.singletonList(
                        new SubmissionFilter(
                                SubmissionFilterClauseEnum.IN,
                                Collections.singletonMap(configuration.getArId(), serviceIds))));

        var caseStatusIdToNameMap = getStatusIdToNameMapByCasesStatuses(caseStatuses);
        var caseStageIdToNameMap = getStageIdToNameMapByCases(projectId, authentication, paginatedCaseSubmissions.getElements());

        var userIds = paginatedCaseSubmissions.getElements()
                .stream()
                .map(r -> (String) r.getData().get(configuration.getRequestorPropertyKey()))
                .distinct()
                .collect(Collectors.toList());
        var userIdToUserNameMap = userProfileService.getUserIdToUserNameMap(projectId, authentication, userIds);

        addCaseExternalProperties(paginatedCaseSubmissions.getElements(), serviceSubmissions, caseStatusIdToNameMap,
                caseStageIdToNameMap, null, userIdToUserNameMap);

        sortCasesByExternalProperties(paginatedCaseSubmissions.getElements(), sort);

        return new Page<>(paginatedCaseSubmissions.getTotalPages(),
                paginatedCaseSubmissions.getTotalElements(),
                paginatedCaseSubmissions.getElements());
    }

    private List<ResourceDto> getCaseStatuses(String projectId, Authentication authentication, String classifier){
        return submissionService.getAllSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getCaseStatusResourcePath()), authentication,
                Collections.singletonList(
                        new SubmissionFilter(
                                SubmissionFilterClauseEnum.IN,
                                Collections.singletonMap(configuration.getCaseStatusClassifierPropertyKey(), classifier))),20L);
    }
    private List<String> getCaseStatusCodes(List<ResourceDto> caseStatuses){
        return caseStatuses
                .stream()
                .map(s -> (String) s.getData().get(CODE_KEY))
                .collect(Collectors.toList());
    }
}

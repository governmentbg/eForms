package com.bulpros.eformsgateway.form.service;

import com.bulpros.eformsgateway.cache.service.CacheService;
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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static com.bulpros.eformsgateway.cache.service.CacheService.*;
import static java.util.Objects.isNull;

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
    public static final String EXIST_SERVICE = "existService";

    private static final String GET_CASE_STATUSES_BY_CLASSIFIER_CACHE = "getCaseStatusesByClassifier";
    private static final String GET_CASE_STATUSES_CACHE = "getCaseStatusesCache";

    private final FormioUserService userService;
    private final UserProfileService userProfileService;
    private final ConfigurationProperties configuration;
    private final SubmissionService submissionService;

    // Do not removed: Used in @Cacheable condition SpEL expression
    @Getter
    private final CacheService cacheService;

    @Override
    public Page<ResourceDto> getAdminCasesByFilterParameters(String projectId, Authentication authentication,
                                                             @Valid AdminCaseFilter caseFilter, Long page, Long size, String sort) {
        List<SubmissionFilter> filters = getAdminFilters(caseFilter);

        var statuses = cacheService.get(GET_CASE_STATUSES_CACHE, caseFilter.getStatusCode().toString(),
                List.class, PUBLIC_CACHE);
        if(isNull(statuses)){
            statuses = getCaseStatuses(projectId, authentication, caseFilter.getStatusCode());
            cacheService.put(GET_CASE_STATUSES_CACHE, caseFilter.getStatusCode().toString(), statuses, PUBLIC_CACHE);
        }
        return getCasePage(projectId, authentication, statuses, page, size, sort, filters);
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

        addCaseExternalProperties(Arrays.asList(caseSubmission), serviceSubmissions, caseStatusIdToNameMap,
                caseStageIdToNameMap, channelTypeToNameMap);

        return caseSubmission;
    }

    @Override
    public Page<ResourceDto> getCasesByUserIdAndStatusClassifier(String projectId, Authentication authentication, String classifier,
                                                                 CaseFilter caseFilter, Long page, Long size, String sort) {

        List<SubmissionFilter> filters = getFilters(caseFilter, projectId, authentication);
        var caseStatuses = getCaseStatusesByClassifier(projectId, authentication, classifier);
        var caseStatusCodes = getCaseStatusCodes(caseStatuses);
        filters.add(new SubmissionFilter(
                SubmissionFilterClauseEnum.IN,
                Collections.singletonMap(configuration.getCaseStatusCodePropertyKey(), caseStatusCodes)));
        return getCasePage(projectId, authentication, caseStatuses, page, size, sort, filters);
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
    public List<ResourceDto> getIncompleteCasesByUserIdAndServiceIds(String projectId, Authentication authentication, String applicant, List<String> serviceIds) {
        var user = userService.getUser(authentication);

        var caseStatuses = cacheService.get(GET_CASE_STATUSES_BY_CLASSIFIER_CACHE, CaseStatusClassifierEnum.SERVICE_IN_COMPLETION.classifier,
                List.class, PUBLIC_CACHE);
        if(isNull(caseStatuses)){
            caseStatuses = getCaseStatusesByClassifier(projectId, authentication,
                            CaseStatusClassifierEnum.SERVICE_IN_COMPLETION.classifier);
            cacheService.put(GET_CASE_STATUSES_BY_CLASSIFIER_CACHE, CaseStatusClassifierEnum.SERVICE_IN_COMPLETION.classifier, caseStatuses, PUBLIC_CACHE);
        }

        var caseStatusCodes = getCaseStatusCodes(caseStatuses);

        List<SubmissionFilter> filters = new ArrayList<>();
        if(StringUtils.isEmpty(applicant)) {
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.IN,
                    Collections.singletonMap(configuration.getRequestorPropertyKey(), user.getPersonIdentifier())));
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.IN,
                    Collections.singletonMap(configuration.getApplicantPropertyKey(), "")));
        }
        else {
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.IN,
                    Collections.singletonMap(configuration.getApplicantPropertyKey(), applicant)));
        }
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

        addCaseExternalProperties(caseSubmissions, serviceSubmissions, caseStatusIdToNameMap,
                caseStageIdToNameMap, null);

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
                .distinct()
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
                                           Map<String, String> channelTypeToNameMap) {
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
                    SubmissionFilterClauseEnum.IN,
                    Collections.singletonMap(configuration.getApplicantPropertyKey(), "")));
        }

        return filters;
    }

    private List<SubmissionFilter> getAdminFilters(AdminCaseFilter caseFilter) {
        List<SubmissionFilter> filters = new ArrayList<>();

        if (caseFilter.getStatusCode() != null && !caseFilter.getStatusCode().isEmpty()) {
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.IN,
                    Collections.singletonMap(configuration.getCaseStatusCodePropertyKey(), caseFilter.getStatusCode())));
        }

        if (!StringUtils.isEmpty(caseFilter.getAdministrationUnitEDelivery())) {
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.NONE,
                    Collections.singletonMap(configuration.getAdministrationUnitEDeliveryKey(),
                            caseFilter.getAdministrationUnitEDelivery())));
        }

        if (!StringUtils.isEmpty(caseFilter.getServiceId())) {
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.NONE,
                    Collections.singletonMap(configuration.getServiceIdPropertyKey(),
                            Collections.singletonList(caseFilter.getServiceId()))));
        }

        if (!StringUtils.isEmpty(caseFilter.getBusinessKey())) {
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.REGEX,
                    Collections.singletonMap(configuration.getBusinessKey(), "(.*)" + caseFilter.getBusinessKey() + "(.*)")));
        }

        if (!StringUtils.isEmpty(caseFilter.getRequestor())) {
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.REGEX,
                    Collections.singletonMap(configuration.getRequestorPropertyKey(), "(.*)" + caseFilter.getRequestor() + "(.*)")));
        }

        if (!StringUtils.isEmpty(caseFilter.getOnBehalfOf())) {
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.REGEX,
                    Collections.singletonMap(configuration.getApplicantPropertyKey(), "(.*)" + caseFilter.getOnBehalfOf() + "(.*)")));
        }

        if (!StringUtils.isEmpty(caseFilter.getFromIssueDate())) {
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.GTE,
                    Collections.singletonMap(configuration.getDeliveryDate(), caseFilter.getFromIssueDate())));
        }

        if (!StringUtils.isEmpty(caseFilter.getToIssueDate())) {
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.LTE,
                    Collections.singletonMap(configuration.getDeliveryDate(), caseFilter.getToIssueDate())));
        }

        if (!StringUtils.isEmpty(caseFilter.getServiceSupplierId())) {
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.IN,
                    Collections.singletonMap(configuration.getServiceSupplierIdPropertyKey(), caseFilter.getServiceSupplierId())));
        }
        return filters;
    }

    private Page<ResourceDto> getCasePage(String projectId, Authentication authentication, List<ResourceDto> statuses, Long page, Long size, String sort, List<SubmissionFilter> filters) {
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

        var caseStatusIdToNameMap = getStatusIdToNameMapByCasesStatuses(statuses);
        var caseStageIdToNameMap = getStageIdToNameMapByCases(projectId, authentication, paginatedCaseSubmissions.getElements());

        addCaseExternalProperties(paginatedCaseSubmissions.getElements(), serviceSubmissions, caseStatusIdToNameMap,
                caseStageIdToNameMap, null);

        sortCasesByExternalProperties(paginatedCaseSubmissions.getElements(), sort);

        return new Page<>(paginatedCaseSubmissions.getTotalPages(),
                paginatedCaseSubmissions.getTotalElements(),
                paginatedCaseSubmissions.getElements());
    }

    @Override
    @Cacheable(value = GET_CASE_STATUSES_BY_CLASSIFIER_CACHE, key = "#classifier", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public List<ResourceDto> getCaseStatusesByClassifier(String projectId, Authentication authentication, String classifier){
        return getCaseStatusesByClassifier(projectId, authentication, classifier, PUBLIC_CACHE);
    }

    @Override
    @Cacheable(value = GET_CASE_STATUSES_BY_CLASSIFIER_CACHE, key = "#classifier", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public List<ResourceDto> getCaseStatusesByClassifier(String projectId, Authentication authentication, String classifier, String cacheControl) {
        return submissionService.getAllSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getCaseStatusResourcePath()), authentication,
                Collections.singletonList(
                        new SubmissionFilter(
                                SubmissionFilterClauseEnum.IN,
                                Collections.singletonMap(configuration.getCaseStatusClassifierPropertyKey(), classifier))),20L);
    }

    @Override
    public String getCaseApplicantByBusinessKey(String projectId, Authentication authentication, String businessKye) {
        ArrayList<SubmissionFilter> filters = new ArrayList<>();
        filters.add(new SubmissionFilter( SubmissionFilterClauseEnum.NONE,
                Collections.singletonMap(configuration.getBusinessKey(), businessKye)));
        filters.add(SubmissionFilter.build("select", "data.applicant"));
        List<ResourceDto> caseResource = submissionService.getAllSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getCaseResourcePath()),
                authentication, filters, 1L, false);
        if(caseResource.isEmpty()) return null;
        return (String) caseResource.get(0).getData().get(configuration.getApplicantPropertyKey());
    }

    @Override
    public String getCaseSupplierByBusinessKey(String projectId, Authentication authentication, String businessKye) {
        ArrayList<SubmissionFilter> filters = new ArrayList<>();
        filters.add(new SubmissionFilter( SubmissionFilterClauseEnum.NONE,
                Collections.singletonMap(configuration.getBusinessKey(), businessKye)));
        filters.add(SubmissionFilter.build("select", "data.supplier"));
        List<ResourceDto> caseResource = submissionService.getAllSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getCaseResourcePath()),
                authentication, filters, 1L, false);
        if(caseResource.isEmpty()) return null;
        return (String) caseResource.get(0).getData().get(configuration.getSupplierPropertyKey());
    }

    @Override
    @Cacheable(value = GET_CASE_STATUSES_CACHE, key = "new org.springframework.cache.interceptor.SimpleKey(statuses)", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public List<ResourceDto> getCaseStatuses(String projectId, Authentication authentication, List<Integer> statuses) {
        return getCaseStatuses(projectId, authentication, statuses, PUBLIC_CACHE);
    }

    @Override
    @Cacheable(value = GET_CASE_STATUSES_CACHE, key = "new org.springframework.cache.interceptor.SimpleKey(statuses)", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public List<ResourceDto> getCaseStatuses(String projectId, Authentication authentication, List<Integer> statuses, String cacheControl) {
        return submissionService.getAllSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getCaseStatusResourcePath()), authentication,
                Collections.singletonList(
                        new SubmissionFilter(
                                SubmissionFilterClauseEnum.IN,
                                Collections.singletonMap(configuration.getCodePropertyKey(), statuses))),20L);

    }

    private List<String> getCaseStatusCodes(List<ResourceDto> caseStatuses){
        return caseStatuses
                .stream()
                .map(s -> (String) s.getData().get(CODE_KEY))
                .collect(Collectors.toList());
    }
}

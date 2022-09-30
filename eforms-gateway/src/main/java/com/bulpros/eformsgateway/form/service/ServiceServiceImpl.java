package com.bulpros.eformsgateway.form.service;

import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_ACTIVE_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_CONTROL_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.PUBLIC_CACHE;
import static java.util.Objects.isNull;

import java.util.*;
import java.util.stream.Collectors;

import com.bulpros.eformsgateway.form.exception.NotSatisfiedAssuranceLevel;
import com.bulpros.eformsgateway.security.service.UserService;
import io.micrometer.core.annotation.Timed;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.bulpros.eformsgateway.cache.service.CacheService;
import com.bulpros.eformsgateway.form.web.controller.dto.AdminServiceFilter;
import com.bulpros.eformsgateway.form.web.controller.dto.ServiceIdAndNameDto;
import com.bulpros.eformsgateway.form.web.controller.dto.ServiceSubmissionResponseDto;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.exception.NotActiveException;
import com.bulpros.formio.exception.ResourceNotFoundException;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.SubmissionFilterClauseEnum;
import com.bulpros.formio.service.SubmissionService;
import com.bulpros.formio.utils.Page;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceServiceImpl implements ServiceService {
    
    public static final String GET_SERVICES_BY_ID_CACHE = "getServicesByIdCache";
    public static final String GET_SERVICE_SUPPLIERS_BY_TITLE_CACHE = "getServiceSuppliersByTitleCache";
    public static final String GET_SERVICE_ASSURANCE_LEVEL_CACHE = "getServiceAssuranceLevelCache";

    public final String CODE_KEY;
    public final String STATUS_KEY;
    public final String AR_ID;

    private final CaseService caseService;
    private final ConfigurationProperties configuration;
    private final SubmissionService submissionService;
    private final UserService userService;
    private final UserProfileService userProfileService;
    private final ValidationServiceService validationServiceService;
    
    // Do not removed: Used in @Cacheable condition SpEL expression
    @Getter private final CacheService cacheService;

    @Autowired
    public ServiceServiceImpl(CaseService caseService, SubmissionService submissionService,
                              ConfigurationProperties configuration,
                              UserProfileService userProfileService, CacheService cacheService, UserService userService, ValidationServiceService validationServiceService) {
        this.caseService = caseService;
        this.configuration = configuration;
        this.submissionService = submissionService;
        this.userProfileService = userProfileService;
        this.cacheService = cacheService;
        this.userService = userService;
        CODE_KEY = configuration.getCodePropertyKey();
        STATUS_KEY = configuration.getStatus();
        AR_ID = configuration.getArId();
        this.validationServiceService = validationServiceService;
    }
    @Override
    @Cacheable(value = GET_SERVICE_ASSURANCE_LEVEL_CACHE, key = "#easId", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public ResourceDto getServiceAssuranceLevel(String projectId, Authentication authentication, String easId) {
            var serviceSubmissions = submissionService.getSubmissionsWithFilter(
                    new ResourcePath(projectId, configuration.getServiceResourcePath()), authentication,
                    List.of(
                            new SubmissionFilter(
                                    SubmissionFilterClauseEnum.NONE,
                                    Collections.singletonMap(configuration.getArId(), easId)),
                            SubmissionFilter.build("select", "data." + configuration.getRequiredSecurityLevel())
                    ),
                    1l, 1l, null, false
            );
        if (serviceSubmissions.getElements() == null || serviceSubmissions.getElements().isEmpty()) {
            log.error("No service with ID = {} is found.", easId);
            throw new ResourceNotFoundException("No service with ID = " + easId + " is found.");
        }

        return serviceSubmissions.getElements().get(0);
    }

    @Override
    public ResourceDto getServiceById(String projectId, Authentication authentication, String easId) {
        return getServiceById(projectId, authentication, easId, PUBLIC_CACHE);
    }

    @Override
    public ResourceDto getServiceById(String projectId, Authentication authentication, String easId, String cacheControl) {
        var resources = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getServiceResourcePath()), authentication,
                Collections.singletonList(
                        new SubmissionFilter(
                                SubmissionFilterClauseEnum.NONE,
                                Collections.singletonMap(configuration.getArId(), easId))));
        if (resources.isEmpty()) throw new ResourceNotFoundException("Service with id: " + easId + " not found!");
        return resources.get(0);
    }

    @Override
    public ServiceSubmissionResponseDto getServiceInfo(String projectId, Authentication authentication, String easId, String applicant) {
        ServiceSubmissionResponseDto response =  cacheService.get(GET_SERVICES_BY_ID_CACHE, easId, ServiceSubmissionResponseDto.class, PUBLIC_CACHE);
        if (isNull(response)) {
            response = getServicesById(projectId, authentication, easId, applicant);
            cacheService.put(GET_SERVICES_BY_ID_CACHE, easId, response, PUBLIC_CACHE);
        }

        var userProfile= userProfileService.getUserProfileData(projectId,authentication);
        validationServiceService.validateRequiredProfile(userProfile, response.getService(), applicant);

        var incompleteCases = caseService.getIncompleteCasesByUserIdAndServiceIds(projectId,
                authentication, applicant, Collections.singletonList(easId));
        var existIncompleteCases =
                (incompleteCases != null && !incompleteCases.isEmpty()) ? Boolean.TRUE : Boolean.FALSE;
        response.setExistIncompleteCases(existIncompleteCases);
        return response;
    }

    @Timed(value = "eforms-gateway-get-services-by-id.time")
    @Override
    @Cacheable(value = GET_SERVICES_BY_ID_CACHE, key = "#easId", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public ServiceSubmissionResponseDto getServicesById(String projectId, Authentication authentication, String easId,
                                                        String applicant) {
        return getServicesById(projectId, authentication, easId, applicant, PUBLIC_CACHE);
    }
        
    @Override
    @Cacheable(value = GET_SERVICES_BY_ID_CACHE, key = "#easId", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public ServiceSubmissionResponseDto getServicesById(String projectId, Authentication authentication, String easId,
                                                        String applicant, String cacheControl) {
        var serviceSubmissions = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getServiceResourcePath()), authentication,
                Collections.singletonList(
                        new SubmissionFilter(
                                SubmissionFilterClauseEnum.NONE,
                                Collections.singletonMap(configuration.getArId(), easId))));

        if (serviceSubmissions == null || serviceSubmissions.isEmpty()) {
            log.error("No service with ID = {} is found.", easId);
            throw new ResourceNotFoundException("No service with ID = " + easId + " is found.");
        }

        var serviceSubmission = serviceSubmissions.get(0);
        var serviceStatus = serviceSubmission.getData().get(STATUS_KEY);
        if (!this.configuration.getStartServiceStatuses().contains(serviceStatus)) {
            throw new NotActiveException("Service with ID: " + easId + " with status: " + serviceStatus + " is not allowed to start");
        }

        serviceSubmission = addFirstTenActiveServiceSuppliers(serviceSubmission, projectId, easId, authentication);
        ServiceSubmissionResponseDto result = new ServiceSubmissionResponseDto();
        result.setService(serviceSubmission);
        return result;
    }

    @Timed(value = "eforms-gateway-get-process-key-by-services-id.time")
    @Override
    public String getProcessKeyByServicesId(String projectId, Authentication authentication, ResourceDto serviceDto) {
        var assuranceLevelString = (String) serviceDto.getData().get(configuration.getRequiredSecurityLevel());
        var serviceRequiredAssuranceLevel = AssuranceLevelEnum.valueOf(assuranceLevelString.toUpperCase(Locale.ROOT));
        if(serviceRequiredAssuranceLevel != null){
            var user= userService.getUser(authentication);
            var userAssuranceLevel = user.getAssuranceLevel();
            if(userAssuranceLevel.getLevel() < serviceRequiredAssuranceLevel.getLevel()){
                throw new NotSatisfiedAssuranceLevel("Assurance level: " + userAssuranceLevel.getType() + " is not satisfied");
            }
        }

        return serviceDto.getData().get(configuration.getProcessDefinitionId()).toString();
    }

    @Override
    public Page<ResourceDto> getServices(String projectId, Authentication authentication,
                                         AdminServiceFilter serviceFilter,
                                         Long page, Long size, String sort) {

        List<SubmissionFilter> filters = new ArrayList<>();
        filters.add(
                new SubmissionFilter(
                        SubmissionFilterClauseEnum.NONE,
                        Collections.singletonMap(configuration.getSupplierEasPropertyKey(), serviceFilter.getServiceSupplierId())
                ));
        if(serviceFilter.getServiceStatuses() != null &&  !serviceFilter.getServiceStatuses().isEmpty()){
            filters.add(
                    new SubmissionFilter(
                            SubmissionFilterClauseEnum.IN,
                            Collections.singletonMap(configuration.getStatus(), serviceFilter.getServiceStatuses())
                    ));
        }

        return submissionService.getSubmissionsWithFilter(new ResourcePath(projectId, configuration.getServiceSuppliersResourcePath()),
                authentication, filters, page, size, sort);
    }

    @Timed(value = "eforms-gateway-get-service-suppliers-by-title.time")
    @Override
    @Cacheable(value = GET_SERVICE_SUPPLIERS_BY_TITLE_CACHE, key = "new org.springframework.cache.interceptor.SimpleKey(#easId, #title)", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public List<ResourceDto> getServiceSuppliersByTitle(String projectId, String easId, String title, Authentication authentication) {
        return getServiceSuppliersByTitle(projectId, easId, title, authentication, PUBLIC_CACHE);
    }

    @Override
    @Cacheable(value = GET_SERVICE_SUPPLIERS_BY_TITLE_CACHE, key = "new org.springframework.cache.interceptor.SimpleKey(#easId, #title)", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public List<ResourceDto> getServiceSuppliersByTitle(String projectId, String easId, String title, Authentication authentication, String cacheControl) {
        var easServiceSuppliers = getFirstTenActiveSuppliersSortedAndFilteredByName(projectId, easId, title, authentication);
        return easServiceSuppliers.getElements();
    }

    @Override
    public List<ServiceIdAndNameDto> getServicesByCaseStatusAndName(String projectId, Authentication authentication,
                                                                    AdminServiceFilter serviceFilter, List<Integer> caseStatuses,
                                                                    String caseAdministrativeUnit, String fromIssueDate, String toIssueDate) {
        List<SubmissionFilter> filters = createCaseSubmissionFilter(serviceFilter);
        filters.add(new SubmissionFilter(
                SubmissionFilterClauseEnum.REGEX,
                Collections.singletonMap(configuration.getAdministrationUnitEDeliveryKey(), "(.*)" + Objects.toString(caseAdministrativeUnit, "") + "(.*)")));
        filters.add(new SubmissionFilter(
                SubmissionFilterClauseEnum.REGEX,
                Collections.singletonMap(configuration.getServiceNamePropertyKey(), "(.*)" + Objects.toString(serviceFilter.getServiceName(), "") + "(.*)")));
        filters.add(new SubmissionFilter(
                SubmissionFilterClauseEnum.IN,
                Collections.singletonMap(configuration.getCaseStatusCodePropertyKey(), caseStatuses)));
        filters.add(SubmissionFilter.build("select", "data." + configuration.getServiceIdPropertyKey()));

        if (StringUtils.isNotEmpty(fromIssueDate)) {
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.GTE,
                    Collections.singletonMap(configuration.getDeliveryDate(), fromIssueDate)));
        }

        if (StringUtils.isNotEmpty(toIssueDate)) {
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.LTE,
                    Collections.singletonMap(configuration.getDeliveryDate(), toIssueDate)));
        }

        var serviceIds = submissionService.getAllSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getCaseResourcePath()), authentication,
                filters,100l, false);

        var servicesIdsWithCases = serviceIds.stream()
                .map(s -> s.getData().get(configuration.getServiceIdPropertyKey()).toString())
                .collect(Collectors.toSet());

        if(servicesIdsWithCases.isEmpty()) {
            return new ArrayList<ServiceIdAndNameDto>();
        }
        var serviceWithCasesFilter = new ArrayList<SubmissionFilter>();
        serviceWithCasesFilter.add(new SubmissionFilter(SubmissionFilterClauseEnum.IN,
                        Map.of(configuration.getArId(), servicesIdsWithCases.stream().distinct().collect(Collectors.toList()))));
        serviceWithCasesFilter.add(SubmissionFilter.build("select", "data." + configuration.getArId()));
        serviceWithCasesFilter.add(SubmissionFilter.build("select", "data." + configuration.getServiceNamePropertyKey()));
        var serviceIdsAndNames = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getServiceResourcePath()),
                authentication, serviceWithCasesFilter, 1L,
                Long.valueOf( servicesIdsWithCases.size()),
                "data." + configuration.getServiceNamePropertyKey(), false);

        return serviceIdsAndNames
                .getElements()
                .stream()
                .map(r -> new ServiceIdAndNameDto(
                        r.getData().get(configuration.getArId()).toString(),
                        r.getData().get(configuration.getServiceNamePropertyKey()).toString()))
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private ResourceDto addAllSuppliersInfo(ResourceDto serviceSubmission, String projectId, Authentication authentication) {
        var easServiceSuppliers = getAllSuppliers(projectId, serviceSubmission.getData().get(AR_ID).toString(), authentication);
        return insertServiceSupplierList(serviceSubmission, easServiceSuppliers);
    }

    private ResourceDto addFirstTenActiveServiceSuppliers(ResourceDto serviceSubmission, String projectId, String easId, Authentication authentication) {
        var easServiceSuppliers = getFirstTenActiveSuppliersSorted(projectId, easId, authentication);
        return insertServiceSupplierList(serviceSubmission, easServiceSuppliers.getElements());
    }

    private ResourceDto insertServiceSupplierList(ResourceDto serviceSubmission, List<ResourceDto> easServiceSuppliers) {
        List<Map<String,Object>> suppliers = new ArrayList<>();
        easServiceSuppliers.forEach(s -> suppliers.add(s.getData()));
        serviceSubmission.getData().put("serviceSupplierList", suppliers);
        return serviceSubmission;
    }

    private Page<ResourceDto> getFirstTenActiveSuppliersSorted(String projectId, String easId, Authentication authentication) {
        var filters = getSubmissionFiltersForActiveSuppliers(easId);
        return submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getServiceSuppliersResourcePath()), authentication,
                filters, 1L, 10L, "data.serviceSupplierTitle", false
        );
    }

    private Page<ResourceDto> getFirstTenActiveSuppliersSortedAndFilteredByName(String projectId, String easId, String title, Authentication authentication) {
        var filters = getSubmissionFiltersForActiveSuppliers(easId);
        var regexFilter = new SubmissionFilter(SubmissionFilterClauseEnum.REGEX, Collections.singletonMap("serviceSupplierTitle", "(.*)" + title + "(.*)"));
        filters.add(regexFilter);
        return submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getServiceSuppliersResourcePath()), authentication,
                filters, 1L, 10L, "data.serviceSupplierTitle", false
        );
    }

    private List<ResourceDto> getAllSuppliers(String projectId, String easId, Authentication authentication) {
        List<SubmissionFilter> filters = getSubmissionFiltersForActiveSuppliersWithStatus(easId);
        return submissionService.getAllSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getServiceSuppliersResourcePath()),
                authentication, filters, 100l, false);
    }

    private List<SubmissionFilter> getSubmissionFiltersForActiveSuppliers(String easId) {
        List<SubmissionFilter> filters = new ArrayList<>();
        filters.add(SubmissionFilter.build("select", "data.serviceSupplierTitle"));
        filters.add(SubmissionFilter.build("select", "data.supplierEAS"));
        filters.add(SubmissionFilter.build(configuration.getArId(), easId));
        filters.add(new SubmissionFilter(SubmissionFilterClauseEnum.IN,
                Map.of(configuration.getStatus(), configuration.getServiceSupplierAllowedStatuses())));
        return filters;
    }

    private List<SubmissionFilter> getSubmissionFiltersForActiveSuppliersWithStatus(String easId) {
        var filters = this.getSubmissionFiltersForActiveSuppliers(easId);
        filters.add(SubmissionFilter.build("select", "data.status"));
        return filters;
    }

    private List<SubmissionFilter> createCaseSubmissionFilter(AdminServiceFilter serviceFilter) {
        List<SubmissionFilter> filters = new ArrayList<>();
        filters.add(
                new SubmissionFilter(
                        SubmissionFilterClauseEnum.NONE,
                        Collections.singletonMap(configuration.getServiceSupplierIdPropertyKey(), serviceFilter.getServiceSupplierId())
                ));
        return filters;
    }

    private List<String> getCaseStatusCodes(String projectId, Authentication authentication, List<Integer> statuses){
        var caseStatusSubmissions = caseService.getCaseStatuses(projectId, authentication,statuses);
        return caseStatusSubmissions
                .stream()
                .map(s -> (String) s.getData().get(CODE_KEY))
                .collect(Collectors.toList());
    }
}

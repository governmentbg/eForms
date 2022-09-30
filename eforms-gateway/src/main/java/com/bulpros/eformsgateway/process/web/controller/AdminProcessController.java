package com.bulpros.eformsgateway.process.web.controller;

import com.bulpros.eformsgateway.cache.service.CacheService;
import com.bulpros.eformsgateway.form.exception.MetadataUpdateFailed;
import com.bulpros.eformsgateway.form.exception.StoreDataException;
import com.bulpros.eformsgateway.form.service.ConfigurationProperties;
import com.bulpros.eformsgateway.form.service.ServiceSupplierService;
import com.bulpros.eformsgateway.form.service.SubmissionPatchService;
import com.bulpros.eformsgateway.form.service.UserProfileService;
import com.bulpros.eformsgateway.form.utils.CommonUtils;
import com.bulpros.eformsgateway.form.web.controller.dto.AdditionalProfileRoleEnum;
import com.bulpros.eformsgateway.form.web.controller.dto.UserProfileDto;
import com.bulpros.eformsgateway.process.service.BusinessKeyService;
import com.bulpros.eformsgateway.process.service.BusinessKeyTypeEnum;
import com.bulpros.eformsgateway.process.service.ProcessService;
import com.bulpros.eformsgateway.process.web.dto.ServiceMetadataDto;
import com.bulpros.eformsgateway.process.web.dto.StartProcessInstanceRequestDto;
import com.bulpros.eformsgateway.process.web.dto.StartProcessInstanceResponseDto;
import com.bulpros.eformsgateway.security.service.UserService;
import com.bulpros.eformsgateway.user.model.User;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.exception.ResourceNotFoundException;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.SubmissionFilterClauseEnum;
import com.bulpros.formio.service.SubmissionService;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.http.HttpHeaders.CACHE_CONTROL;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminProcessController extends AbstractProcessController {

    private final UserService userService;
    private final ConfigurationProperties configurationProperties;
    private final ServiceSupplierService serviceSupplierService;
    private final SubmissionService submissionService;
    private final SubmissionPatchService submissionPatchService;
    private final CacheService cacheService;

    public AdminProcessController(UserService userService, BusinessKeyService businessKeyService,
                                  ProcessService processService, UserProfileService userProfileService,
                                  ConfigurationProperties configurationProperties, ServiceSupplierService serviceSupplierService, SubmissionService submissionService, SubmissionPatchService submissionPatchService, CacheService cacheService) {
        super(userProfileService, businessKeyService, processService);
        this.userService = userService;
        this.configurationProperties = configurationProperties;
        this.serviceSupplierService = serviceSupplierService;
        this.submissionService = submissionService;
        this.submissionPatchService = submissionPatchService;
        this.cacheService = cacheService;
    }

    @PostMapping("/start-process/{process-key}")
    public ResponseEntity<StartProcessInstanceResponseDto> startProcess(Authentication authentication,
                                                                        @PathVariable("process-key") String processKey,
                                                                        @RequestHeader(CACHE_CONTROL) String cacheControl,
                                                                        @RequestBody StartProcessInstanceRequestDto startProcessInstanceRequestDto,
                                                                        @RequestParam(value = "applicant", required = false) String applicant) {
        if (!configurationProperties.isMetadataProcess(processKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        if (applicant == null || ObjectUtils.isEmpty(applicant)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        var userProfile = getUserProfile(authentication, startProcessInstanceRequestDto);
        if (!checkAuthorization(userProfile, applicant, AdditionalProfileRoleEnum.MetadataManager)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        User user = userService.getUser(authentication);
        startProcessInstanceRequestDto.setBusinessKeyType(BusinessKeyTypeEnum.GENERATED);
        startProcessInstanceRequestDto.setApplicant(applicant);
        return startProcess(authentication, processKey, startProcessInstanceRequestDto, applicant, true,
                userProfile, user,
                null, cacheControl);

    }

    @Timed(value = "eforms-gateway-store-metadata.time")
    @PostMapping("/projects/{projectId}/metadata/update")
    public ResponseEntity<Void> storeMetadata(Authentication authentication,
                                              @PathVariable("projectId") String projectId,
                                              @RequestBody ServiceMetadataDto serviceMetadataDto,
                                              @RequestParam(value = "applicant", required = true) String applicant) {

        if (StringUtils.isEmpty(applicant)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        UserProfileDto userProfile = null;

        userProfile = this.userProfileService.getUserProfileData(projectId, authentication);
        var commonNomSuppliers = serviceMetadataDto.getCommonNomEasSuppliers();
        var commonResourcesTermAndTaxes = serviceMetadataDto.getCommonResourceTermTaxes();

        if (this.userProfileService.hasUserRole(userProfile, applicant, AdditionalProfileRoleEnum.MetadataManager)) {
            var supplier = serviceSupplierService.getServiceSupplierByEik(projectId, authentication, applicant);
            if (supplier == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

            if (!applicant.equals(supplier.getEik())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            if (!commonNomSuppliers.get(configurationProperties.getArId())
                    .equals(commonResourcesTermAndTaxes.get(configurationProperties.getArId()))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            var supplierCode = supplier.getCode();
            var requestSupplierCode = (String) commonNomSuppliers.get(configurationProperties.getSupplierEasPropertyKey());
            var supplierTermAndTaxesCode = (String) commonResourcesTermAndTaxes.get(configurationProperties.getSupplierEasPropertyKey());

            if (StringUtils.isEmpty(supplierCode) || StringUtils.isEmpty(requestSupplierCode)
                    || StringUtils.isEmpty(supplierTermAndTaxesCode)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            if (!supplierCode.equals(requestSupplierCode) && !supplierCode.equals(supplierTermAndTaxesCode)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        var serviceWithSupplierResourcePath = new ResourcePath(projectId, configurationProperties.getServiceSuppliersResourcePath());
        var termWithTaxesResourcePath = new ResourcePath(projectId, configurationProperties.getTermTaxesResourcePath());
        var historyMetadataResourcePath = new ResourcePath(projectId, configurationProperties.getMetadataHistoryResourcePath());
        var serviceWithSupplier = getServiceWithSupplier(serviceWithSupplierResourcePath, authentication, commonNomSuppliers);
        var termAndTaxes = getTermAndTaxes(termWithTaxesResourcePath, authentication, commonResourcesTermAndTaxes);
        ResourceDto serviceWithSupplierUpdated;
        try {
            updateResource(serviceWithSupplierResourcePath, authentication, serviceWithSupplier, commonNomSuppliers,
                    List.of(configurationProperties.getArId(), configurationProperties.getSupplierEasPropertyKey()));
        } catch (StoreDataException e) {
            throw new MetadataUpdateFailed("Metadata store procedure failed! Reason: " + e.getMessage());
        }
        try {
            updateResource(termWithTaxesResourcePath, authentication, termAndTaxes, commonResourcesTermAndTaxes,
                    List.of(configurationProperties.getArId(), configurationProperties.getSupplierEasPropertyKey()));

            uploadHistoryData(historyMetadataResourcePath, authentication, applicant, serviceMetadataDto);
        } catch (StoreDataException e) {
            updateResource(serviceWithSupplierResourcePath, authentication, serviceWithSupplier,
                (HashMap<String, Object>) serviceWithSupplier.getData(),
                List.of(configurationProperties.getArId(), configurationProperties.getSupplierEasPropertyKey()));
            throw new MetadataUpdateFailed("Metadata store procedure failed! Reason: " + e.getMessage());
        }
        cacheService.invalidateAllCaches();
        return ResponseEntity.ok().build();
    }

    protected boolean checkAuthorization(UserProfileDto userProfile, String applicant, AdditionalProfileRoleEnum additionalProfileRoleEnum) {
        return this.userProfileService.hasUserRole(userProfile, applicant, additionalProfileRoleEnum);
    }

    private ResourceDto updateResource(ResourcePath path, Authentication authentication, ResourceDto oldSubmission,
                                       HashMap<String, Object> newSubmissionData, List<String> notUpdatedProperties) {

        try {
            if (oldSubmission != null) {
                for (String param : notUpdatedProperties) {
                    newSubmissionData.remove(param);
                }
                return submissionService.updateSubmission(path, authentication, oldSubmission.get_id(), submissionPatchService.createPatchData(newSubmissionData));
            }
            return submissionService.createSubmission(path, authentication, CommonUtils.getJsonString(newSubmissionData));
        } catch (Exception exception) {
            throw new StoreDataException(exception.getMessage());
        }
    }

    private ResourceDto uploadHistoryData(ResourcePath path, Authentication authentication, String applicant,
                                          ServiceMetadataDto serviceMetadata) {
        HashMap<String, Object> historyData = new HashMap<>();
        var user = userService.getUser(authentication);
        historyData.put("personIdentifier", user.getPersonIdentifier());
        historyData.put("applicant", applicant);
        historyData.put("metadata", serviceMetadata);
        return submissionService.createSubmission(path, authentication, CommonUtils.getJsonString(historyData));
    }

    private ResourceDto getServiceWithSupplier(ResourcePath path, Authentication authentication, HashMap<String, Object> commonNomSuppliers) {
        List<ResourceDto> submissionServiceWithSupplier = submissionService.getSubmissionsWithFilter(
                path, authentication, List.of(
                        getSubmissionFilterWithArIdAndSupplierCode((String) commonNomSuppliers.get(configurationProperties.getArId()),
                                (String) commonNomSuppliers.get(configurationProperties.getSupplierEasPropertyKey()))));
        if (submissionServiceWithSupplier.isEmpty())
            throw new ResourceNotFoundException("Service: " +
                    commonNomSuppliers.get(configurationProperties.getArId()) + " with supplier: " +
                    commonNomSuppliers.get(configurationProperties.getSupplierEasPropertyKey())
                    + " not found!");
        return submissionServiceWithSupplier.get(0);
    }

    private ResourceDto getTermAndTaxes(ResourcePath path, Authentication authentication, HashMap<String, Object> commonResourcesTermAndTaxes) {
        List<ResourceDto> submissionServiceWithSupplier = submissionService.getSubmissionsWithFilter(
                path, authentication, List.of(
                        getSubmissionFilterWithArIdAndSupplierCode((String) commonResourcesTermAndTaxes.get(configurationProperties.getArId()),
                                (String) commonResourcesTermAndTaxes.get(configurationProperties.getSupplierEasPropertyKey()))));
        if (submissionServiceWithSupplier.isEmpty()) return null;
        return submissionServiceWithSupplier.get(0);
    }

    private SubmissionFilter getSubmissionFilterWithArIdAndSupplierCode(String arId, String serviceCode) {
        return new SubmissionFilter(
                SubmissionFilterClauseEnum.NONE,
                Map.of(configurationProperties.getArId(), arId,
                        configurationProperties.getSupplierEasPropertyKey(), serviceCode));
    }
}

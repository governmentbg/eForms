package com.bulpros.eformsgateway.form.service;

import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_ACTIVE_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_CONTROL_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.PUBLIC_CACHE;
import static java.util.Objects.nonNull;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.SneakyThrows;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.bulpros.eformsgateway.cache.service.CacheService;
import com.bulpros.eformsgateway.form.web.controller.dto.ServiceSupplierDto;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.SubmissionFilterClauseEnum;
import com.bulpros.formio.service.SubmissionService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceSupplierServiceImpl implements ServiceSupplierService {
    
    public static final String GET_SERVICE_SUPPLIER_BY_EIK_CACHE = "getServiceSupplierByEik";
    public static final String GET_SUPPLIER_WITH_ADMIN_UNITS_BY_CODE_CACHE = "getSupplierWithAdminUnitsByCodeCache";
    public static final String GET_SUPPLIER_WITH_CHANNELS_TERMS_AND_TAXES_CACHE = "getServiceSupplierWithChannelTermsAndTaxes";
    public static final String GET_SERVICE_SUPPLIER_WITH_METADATA_CACHE = "getServiceSupplierMetadata";

    public static final String USE_SUPPLIER_CHANNELS_AND_TERMS_LIST = "useSupplierChannelsAndTermsList";
    
    private final ConfigurationProperties configuration;
    private final SubmissionService submissionService;
    // Do not removed: Used in @Cacheable condition SpEL expression
    @Getter private final CacheService cacheService;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final String YES = "yes";

    @Override
    @Cacheable(value = GET_SERVICE_SUPPLIER_BY_EIK_CACHE, key = "#eik", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public ServiceSupplierDto getServiceSupplierByEik(String projectId, Authentication authentication, String eik) {
        return getServiceSupplierByEik(projectId, authentication, eik, PUBLIC_CACHE);
    }

    @Override
    @Cacheable(value = GET_SERVICE_SUPPLIER_BY_EIK_CACHE, key = "#eik", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public ServiceSupplierDto getServiceSupplierByEik(String projectId, Authentication authentication, String eik, String cacheControl) {
        var resources = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getProviderResourcePath()), authentication,
                Collections.singletonList(
                        new SubmissionFilter(
                                SubmissionFilterClauseEnum.NONE,
                                Collections.singletonMap(configuration.getEikPropertyKey(), eik))));
        if (resources.isEmpty()) return null;
        return modelMapper.map(resources.get(0).getData(), ServiceSupplierDto.class);
    }

    @Override
    @Cacheable(value = GET_SUPPLIER_WITH_ADMIN_UNITS_BY_CODE_CACHE, key = "new org.springframework.cache.interceptor.SimpleKey(#easId, #code)", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public ResourceDto getSupplierWithAdminUnitsByCode(String projectId, Authentication authentication, String easId, String code) {
        return getSupplierWithAdminUnitsByCode(projectId, authentication, easId, code, PUBLIC_CACHE);
    }

    @Override
    @Cacheable(value = GET_SUPPLIER_WITH_ADMIN_UNITS_BY_CODE_CACHE, key = "new org.springframework.cache.interceptor.SimpleKey(#easId, #code)", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public ResourceDto getSupplierWithAdminUnitsByCode(String projectId, Authentication authentication, String easId, String code, String cacheControl) {
        var filter = getServiceWithSupplierFilter(easId, code);
        var serviceWithSuppliers = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getServiceSuppliersResourcePath()),
                authentication, filter);
        if(serviceWithSuppliers.isEmpty()) return null;
        var supplierFilter = getSupplierFilter(code);
        var suppliers = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getProviderResourcePath()),
                authentication, supplierFilter);
        if(suppliers.isEmpty()) return null;
        var supplier = suppliers.get(0);
        var dataServiceWithSupplier = serviceWithSuppliers.get(0).getData();
        if(supplier.getData().get(configuration.getHasAdministrativeUnits()).toString().equals(YES)) {
            var administrativeUnits = dataServiceWithSupplier
                    .get(configuration.getAdministrativeUnitsList());
            supplier.getData().put(configuration.getAdministrativeUnitsList(), administrativeUnits);
        }
        supplier.getData().put(configuration.getAisClientEPayment(), dataServiceWithSupplier.get(configuration.getAisClientEPayment()));
        supplier.getData().put(configuration.getHasFixedPayment(), dataServiceWithSupplier.get(configuration.getHasFixedPayment()));
        supplier.getData().put(configuration.getHasPayment(), dataServiceWithSupplier.get(configuration.getHasPayment()));
        supplier.getData().put(configuration.getProcessingKey(), dataServiceWithSupplier.get(configuration.getProcessingKey()));
        supplier.getData().put(configuration.getServiceDescription(), dataServiceWithSupplier.get(configuration.getServiceDescription()));
        supplier.getData().put(configuration.getStatus(), dataServiceWithSupplier.get(configuration.getStatus()));
        return supplier;
    }

    @Override
    @Cacheable(value = GET_SERVICE_SUPPLIER_WITH_METADATA_CACHE, key = "new org.springframework.cache.interceptor.SimpleKey(#easId, #code)", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public ResourceDto getServiceSupplierMetadata(String projectId, Authentication authentication, String easId, String code) {
        return getServiceSupplierMetadata(projectId, authentication, easId, code, PUBLIC_CACHE);
    }

    @Override
    @SneakyThrows(value = JsonProcessingException.class)
    @Cacheable(value = GET_SERVICE_SUPPLIER_WITH_METADATA_CACHE, key = "new org.springframework.cache.interceptor.SimpleKey(#easId, #code)", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public ResourceDto getServiceSupplierMetadata(String projectId, Authentication authentication, String easId, String code, String cacheControl) {
        var serviceSupplierWithChannelTermsAndTaxes = this.getServiceSupplierWithChannelTermsAndTaxes(projectId, authentication,easId, code);

        var serviceSupplierWithAdministrativeUnits = this.getSupplierWithAdminUnitsByCode(projectId, authentication,easId, code);
        if (nonNull(serviceSupplierWithAdministrativeUnits)) {
            var serviceSupplierWithAdministrativeUnitsJson = objectMapper.writeValueAsString(serviceSupplierWithAdministrativeUnits);
            JSONArray administrativeUnits = JsonPath.read(serviceSupplierWithAdministrativeUnitsJson, "$..data.administrativeUnitsList.*");
            administrativeUnits.forEach(adminUnit -> {
                var channelsAndTermsListAdminUnit = ((ArrayList)((LinkedHashMap)adminUnit)
                        .get(configuration.getChannelsAndTermsList()));
                if(channelsAndTermsListAdminUnit.isEmpty()){
                    ((LinkedHashMap) adminUnit).put(USE_SUPPLIER_CHANNELS_AND_TERMS_LIST, true);
                }
                else {
                    ((LinkedHashMap) adminUnit).put(USE_SUPPLIER_CHANNELS_AND_TERMS_LIST, false);
                    channelsAndTermsListAdminUnit.removeIf(o -> !isActivePeriod((LinkedHashMap) o));
                    administrativeUnits.set(administrativeUnits.indexOf(adminUnit), adminUnit);
                }
            });
            serviceSupplierWithAdministrativeUnits.getData().put(configuration.getAdministrativeUnitsList(), administrativeUnits);
        }
        if (nonNull(serviceSupplierWithAdministrativeUnits) && nonNull(serviceSupplierWithChannelTermsAndTaxes)){
            var channelsAndTermsList = (ArrayList)serviceSupplierWithChannelTermsAndTaxes.getData().get(configuration.getChannelsAndTermsList());
            var validChannelsAndTerms = channelsAndTermsList.stream().filter(termAndTaxes -> isActivePeriod((LinkedHashMap)termAndTaxes)).collect(Collectors.toList());
            serviceSupplierWithChannelTermsAndTaxes.getData().put(configuration.getChannelsAndTermsList(), validChannelsAndTerms);
            serviceSupplierWithAdministrativeUnits.getData().put(configuration.getChannelsAndTermsList(), serviceSupplierWithChannelTermsAndTaxes.getData().get(configuration.getChannelsAndTermsList()));
        }
        return serviceSupplierWithAdministrativeUnits;
    }

    public boolean isActivePeriod(LinkedHashMap channelWithTermAndTaxes) {
        var now = Instant.now();
        var validFrom = (String)channelWithTermAndTaxes.get(configuration.getValidFrom());
        var validTo =  (String)channelWithTermAndTaxes.get(configuration.getValidTo());
        if(StringUtils.isEmpty(validFrom)) return false;

        var validAfter = Instant.parse(validFrom).equals(now) || Instant.parse(validFrom).isBefore(now);
        if(!validAfter) return false;
        if(validAfter && StringUtils.isEmpty(validTo)) return true;

        var validТоCheck = Instant.parse(validTo).equals(now) || Instant.parse(validTo).isAfter(now);

        if(validAfter && validТоCheck) return true;

        return false;
    }

    @Override
    @Cacheable(value = GET_SUPPLIER_WITH_CHANNELS_TERMS_AND_TAXES_CACHE, key = "new org.springframework.cache.interceptor.SimpleKey(#easId, #code)", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public ResourceDto getServiceSupplierWithChannelTermsAndTaxes(String projectId, Authentication authentication, String easId, String code) {
        return this.getServiceSupplierWithChannelTermsAndTaxes(projectId, authentication, easId, code, PUBLIC_CACHE);
    }

    @Override
    @Cacheable(value = GET_SUPPLIER_WITH_CHANNELS_TERMS_AND_TAXES_CACHE, key = "new org.springframework.cache.interceptor.SimpleKey(#easId, #code)", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public ResourceDto getServiceSupplierWithChannelTermsAndTaxes(String projectId, Authentication authentication, String easId, String code, String cacheControl) {
        var filter = getSupplierWithChannelTermsAndTaxes(easId, code);
        var submission =  submissionService.getAllSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getTermTaxesResourcePath()),
                authentication, filter, 1L, false);
        if(submission.isEmpty()) return null;
        return submission.get(0);
    }

    private List<SubmissionFilter> getSupplierWithChannelTermsAndTaxes(String easId, String code) {
        var filters = new ArrayList<SubmissionFilter>();
        filters.add(getEasIdAndCodeSubmissionFilter(easId, code));
        filters.add(SubmissionFilter.build("select", "data." + configuration.getChannelsAndTermsList()));
        return filters;
    }

    private List<SubmissionFilter> getSupplierFilter(String code) {
        List<SubmissionFilter> filters = new ArrayList<>();
        filters.add( new SubmissionFilter(
                SubmissionFilterClauseEnum.NONE,
                Collections.singletonMap(configuration.getCodePropertyKey(), code)));
        return filters;
    }

    private List<SubmissionFilter> getServiceWithSupplierFilter(String easId, String code) {
        var filters = new ArrayList<SubmissionFilter>();
        filters.add(getEasIdAndCodeSubmissionFilter(easId, code));
        filters.add(new SubmissionFilter(
                SubmissionFilterClauseEnum.IN,
                Map.of(configuration.getStatus(), configuration.getServiceSupplierAllowedStatuses())
        ));
        return filters;
    }

    @NotNull
    private SubmissionFilter getEasIdAndCodeSubmissionFilter(String easId, String code) {
        return new SubmissionFilter(
                SubmissionFilterClauseEnum.NONE,
                Map.of(
                        configuration.getArId(), easId,
                        configuration.getSupplierEasPropertyKey(), code

                ));
    }

}

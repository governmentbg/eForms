package com.bulpros.eformsgateway.form.service;

import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_ACTIVE_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_CONTROL_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.PUBLIC_CACHE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_CONTROL_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.PUBLIC_CACHE;

@Service
@RequiredArgsConstructor
public class ServiceSupplierServiceImpl implements ServiceSupplierService {
    
    public static final String GET_SERVICE_SUPPLIER_BY_EIK_CACHE = "getServiceSupplierByEik";
    public static final String GET_SUPPLIER_WITH_ADMIN_UNITS_BY_CODE_CACHE = "getSupplierWithAdminUnitsByCodeCache";
    
    private final ConfigurationProperties configuration;
    private final SubmissionService submissionService;
    // Do not removed: Used in @Cacheable condition SpEL expression
    @Getter private final CacheService cacheService;
    private final ModelMapper modelMapper;
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

    private List<SubmissionFilter> getSupplierFilter(String code) {
        List<SubmissionFilter> filters = new ArrayList<>();
        filters.add( new SubmissionFilter(
                SubmissionFilterClauseEnum.NONE,
                Collections.singletonMap(configuration.getCodePropertyKey(), code)));
        return filters;
    }

    private List<SubmissionFilter> getServiceWithSupplierFilter(String easId, String code) {
        var filters = new ArrayList<SubmissionFilter>();
        filters.add(new SubmissionFilter(
                SubmissionFilterClauseEnum.NONE,
                Map.of(
                        configuration.getArId(), easId,
                        configuration.getSupplierEasPropertyKey(), code

                )));
        filters.add(new SubmissionFilter(
                SubmissionFilterClauseEnum.IN,
                Map.of(configuration.getStatus(), List.of(ServiceStatusEnum.ACTIVE.status,
                        ServiceStatusEnum.DRAFT.status))
        ));
        return filters;
    }

}

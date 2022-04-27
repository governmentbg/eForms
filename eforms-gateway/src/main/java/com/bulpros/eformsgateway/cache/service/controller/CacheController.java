package com.bulpros.eformsgateway.cache.service.controller;

import com.bulpros.eformsgateway.cache.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class CacheController {
    
    private final CacheService cacheService;

    @DeleteMapping(path = "/caches", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> invalidateCash() {

        cacheService.invalidateAllCaches();
        return ResponseEntity.ok().build();
    }

}

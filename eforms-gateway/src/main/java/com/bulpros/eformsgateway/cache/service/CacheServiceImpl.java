package com.bulpros.eformsgateway.cache.service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {
    
    @Value("${active-caches}")
    private final List<String> activeCaches;
    
    private final CacheManager cacheManager;
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String cacheName, String key, Class<T> type, String cacheControl) {
        var cache = getCache(cacheName);
        if (isNull(cache.get(key)) || isNull(cache.get(key).get()) || !isCacheEnabled(cacheName, cacheControl))
            return null;
        return (T) cache.get(key).get();
    }
    
    @Override
    public <T> T put(String cacheName, String key, T value, String cacheControl) {
        if (isCacheEnabled(cacheName, cacheControl))
            getCache(cacheName).put(key, value);
        return value;
    }
    
    @Override
    public <T> T putIfPresent(final String cacheName, final String key, final T value, String cacheControl) {
        var cache = getCache(cacheName);
        if (isNull(value) || isNull(cache.get(key)) || isNull(cache.get(key).get()))
            return value;
        return put(cacheName, key, value, cacheControl);
    }
    
    @Override
    public <T> T putIfAbsent(final String cacheName, final String key, final T value, String cacheControl) {
        if (isCacheEnabled(cacheName, cacheControl))
            getCache(cacheName).putIfAbsent(key, value);
        return value;
    }
    
    @Override
    public void evict(final String cacheName, final String key) {
        getCache(cacheName).evict(key);
    }
    
    /**
     * Memcached implementation not support evictIfPresent method provided by Spring Interface.
     */
    @Override
    public boolean evictIfPresent(final String cacheName, final String key) {
        final boolean isPresentInCache = nonNull(getCache(cacheName).get(key));
        if (isPresentInCache) {
            evict(cacheName, key);
        }
        return isPresentInCache;
    }
    
    @Override
    public void invalidateCache(final String cacheName) {
        getCache(cacheName).invalidate();
    }
    
    @Override
    public void invalidateAllCaches() {
        cacheManager.getCacheNames().forEach(this::invalidateCache);
    }
    
    @Override
    public boolean isCacheActive(final String cacheName) {
        return nonNull(activeCaches) && activeCaches.contains(cacheName);
    }
    
    @Override
    public boolean isCacheEnabled(final String cacheName, final String cacheControl) {
        return isCacheActive(cacheName) && (isNull(cacheControl) || !cacheControl.equalsIgnoreCase(NO_CACHE));
    }
    
    private Cache getCache(final String cacheName) {
        return cacheManager.getCache(cacheName);
    }
}

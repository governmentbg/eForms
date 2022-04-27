package com.bulpros.eformsgateway.cache.service;

public interface CacheService {
    
    public static final String PUBLIC_CACHE = "public";
    public static final String NO_CACHE = "no-cache";
    public static final String CACHE_ACTIVE_CONDITION = "#root.target.cacheService.isCacheActive(#root.caches[0].name)";
    public static final String CACHE_CONTROL_CONDITION = "#root.target.cacheService.isCacheEnabled(#root.caches[0].name, #cacheControl)";
    
    <T> T get(final String cacheName, final String key, final Class<T> type, final String cacheControl);
    <T> T put(final String cacheName, final String key, final T value, final String cacheControl);
    <T> T putIfPresent(final String cacheName, final String key, final T value, final String cacheControl);
    <T> T putIfAbsent(final String cacheName, final String key, final T value, final String cacheControl);
    void evict(String cacheName, String key);
    boolean evictIfPresent(String cacheName, String key);
    void invalidateCache(String cacheName);
    void invalidateAllCaches();
    boolean isCacheActive(final String cacheName);
    boolean isCacheEnabled(final String cacheName, final String cacheControl);
}

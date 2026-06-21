package com.mhirro.weather.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;

@Slf4j
@Configuration
@EnableCaching
@EnableScheduling
public class CachingConfiguration {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                new ConcurrentMapCache("primary"),
                new ConcurrentMapCache("secondary")
        ));

        return cacheManager;
    }

    @CacheEvict(allEntries = true, value = {"primary"})
    @Scheduled(fixedDelayString = "3s", initialDelayString = "3s")
    public void clearCache() {
        log.info("Cache cleared");
    }
}

package com.bulpros.eforms.processengine.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class GenerateFilesPackageThreadPoolConfig {

    @Value("${com.bulpros.generate.files.package.core.pool.size}")
    private int corePoolSize;
    @Value("${com.bulpros.generate.files.package.max.pool.size}")
    private int maxPoolSize;

    @Bean("generateFilesPackageTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("GenerateFilesPackage-");
        return executor;

    }
}

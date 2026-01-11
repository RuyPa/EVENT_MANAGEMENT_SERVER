package com.mobile_app_server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class TaskExecutorConfig {

    @Value("${pcrf.task.executor.core-pool-size}")
    private int corePoolSize;

    @Value("${pcrf.task.executor.max-pool-size}")
    private int maxPoolSize;

    @Value("${pcrf.task.executor.queue-capacity}")
    private int queueCapacity;

    @Value("${pcrf.task.executor.keep-alive-seconds}")
    private int keepAliveSeconds;

    @Bean("pcrfTaskExecutor")
    public TaskExecutor pcrfTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix("pcrf-exec-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();
        return executor;
    }
}

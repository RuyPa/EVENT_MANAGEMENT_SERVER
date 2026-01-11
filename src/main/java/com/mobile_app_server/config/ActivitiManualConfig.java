package com.mobile_app_server.config;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class ActivitiManualConfig {

    @Autowired(required = false)
    private DataSource dataSource; // nếu null -> engine sẽ dùng in-memory H2 if you configure so

    @Autowired(required = false)
    private PlatformTransactionManager transactionManager;

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration() throws Exception {
        SpringProcessEngineConfiguration cfg = new SpringProcessEngineConfiguration();

        if (dataSource != null) {
            cfg.setDataSource(dataSource);
        } // else you can set jdbc url, driver, user here

        if (transactionManager != null) {
            cfg.setTransactionManager(transactionManager);
        }

        cfg.setDatabaseSchemaUpdate(SpringProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        cfg.setAsyncExecutorActivate(false);

        // auto-deploy bpmn files from classpath:/processes/
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        cfg.setDeploymentResources(resolver.getResources("classpath:/processes/*.bpmn20.xml"));

        return cfg;
    }

    @Bean
    public ProcessEngineFactoryBean processEngine(SpringProcessEngineConfiguration configuration) {
        ProcessEngineFactoryBean factory = new ProcessEngineFactoryBean();
        factory.setProcessEngineConfiguration(configuration);
        return factory;
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngineFactoryBean factoryBean) throws Exception {
        ProcessEngine engine = factoryBean.getObject();
        return engine.getRuntimeService();
    }
}
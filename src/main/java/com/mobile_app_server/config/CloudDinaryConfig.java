package com.mobile_app_server.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class CloudDinaryConfig {

    @Bean
    @Scope("singleton")
    public Cloudinary getCloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dkf74ju3o",
                "api_key", "639453249624293",
                "api_secret", "2GY34a7PT11RkkaTwEsKP9eYkwI",
                "secure", true));
    }
}

package com.egonzalias.customer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

@Configuration
public class SqsConfig {

    @Bean
    public MappingJackson2MessageConverter messageConverter() {
        MappingJackson2MessageConverter converter =
                new MappingJackson2MessageConverter();

        converter.setSerializedPayloadClass(String.class);
        converter.setStrictContentTypeMatch(false);

        return converter;
    }
}
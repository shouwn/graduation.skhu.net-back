package com.shouwn.graduation.config

import org.modelmapper.Conditions
import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ModelMapperConfig{

    @Bean
    fun modelMapper(): ModelMapper =
            ModelMapper().apply {
                configuration.propertyCondition = Conditions.isNotNull()
            }
}
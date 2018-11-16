package com.shouwn.graduation

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    org.neo4j.ogm.config.ObjectMapperFactory.objectMapper()
            .registerModule(JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

    runApplication<Application>(*args){
        setDefaultProperties(
                mapOf("spring.config.location"
                        to "classpath:/application.yml, /app/config/graduation/application.yml"))
    }
}

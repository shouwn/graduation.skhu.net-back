package com.shouwn.graduation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import java.util.HashMap


@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args){
        setDefaultProperties(
                mapOf("spring.config.location"
                        to "classpath:/application.yml, /app/config/graduation/application.yml"))
    }
}

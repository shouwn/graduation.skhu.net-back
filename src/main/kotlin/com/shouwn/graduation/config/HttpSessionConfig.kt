//package com.shouwn.graduation.config
//
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession
//import org.springframework.session.web.http.HeaderHttpSessionIdResolver
//import org.springframework.session.web.http.HttpSessionIdResolver
//
//@Configuration
//@EnableRedisHttpSession
//class HttpSessionConfig constructor(
//        @Value("\${spring.redis.host}") val redisHost: String,
//        @Value("\${spring.redis.port}") val redisPort: Int
//){
//
//    @Bean
//    fun connectionFactory(): LettuceConnectionFactory = LettuceConnectionFactory(redisHost, redisPort)
//
//    @Bean
//    fun httpSessionIdResolver(): HttpSessionIdResolver = HeaderHttpSessionIdResolver.xAuthToken()
//
//}
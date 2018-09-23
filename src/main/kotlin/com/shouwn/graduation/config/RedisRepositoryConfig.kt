//package com.shouwn.graduation.config
//
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.data.redis.connection.RedisConnectionFactory
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
//import org.springframework.data.redis.core.RedisTemplate
//import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
//
//@Configuration
//@EnableRedisRepositories
//class RedisRepositoryConfig constructor(
//        @Value("\${spring.redis.host}") val redisHost: String,
//        @Value("\${spring.redis.port}") val redisPort: Int
//){
//    @Bean
//    fun redisConnectionFactory(): RedisConnectionFactory {
//        return LettuceConnectionFactory(redisHost, redisPort)
//    }
//
//    @Bean
//    fun redisTemplate(): RedisTemplate<*, *> {
//        val redisTemplate = RedisTemplate<ByteArray, ByteArray>()
//        redisTemplate.setConnectionFactory(redisConnectionFactory())
//        return redisTemplate
//    }
//}
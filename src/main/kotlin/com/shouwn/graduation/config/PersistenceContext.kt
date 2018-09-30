package com.shouwn.graduation.config

import org.neo4j.ogm.session.SessionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@ComponentScan("com.shouwn.graduation")
@EnableNeo4jRepositories("com.shouwn.graduation.repository")
class PersistenceContext{

    @Value("\${spring.data.neo4j.uri}")
    lateinit var uri: String

    @Value("\${spring.data.neo4j.username}")
    lateinit var username: String

    @Value("\${spring.data.neo4j.password}")
    lateinit var password: String

    @Bean
    fun sessionFactory(): SessionFactory {
        return SessionFactory(configuration(), "com.shouwn.graduation.model.domain.entity")
    }

    @Bean
    @Throws(Exception::class)
    fun transactionManager(): Neo4jTransactionManager {
        return Neo4jTransactionManager(sessionFactory())
    }

    @Bean
    fun configuration(): org.neo4j.ogm.config.Configuration {
        return org.neo4j.ogm.config.Configuration.Builder()
                .uri(uri)
                .credentials(username, password)
                .build()
    }
}
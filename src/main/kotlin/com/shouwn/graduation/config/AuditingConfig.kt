package com.shouwn.graduation.config

import com.shouwn.graduation.security.UserPrincipal
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.neo4j.annotation.EnableNeo4jAuditing
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@Configuration
@EnableNeo4jAuditing
class AuditingConfig {

    @Bean
    fun auditorProvider(): AuditorAware<Long> = SpringSecurityAuditAwareImpl()
}

class SpringSecurityAuditAwareImpl : AuditorAware<Long> {

    override fun getCurrentAuditor(): Optional<Long> {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication

        if(authentication?.isAuthenticated != true ||
                authentication is AnonymousAuthenticationToken)
            return Optional.empty()

        val userPrincipal = authentication.principal as UserPrincipal

        return Optional.ofNullable(userPrincipal.id)
    }
}
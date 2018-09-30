package com.shouwn.graduation.config

import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.security.CustomUserDetailsService
import com.shouwn.graduation.security.JwtAuthenticationEntryPoint
import com.shouwn.graduation.security.JwtAuthenticationFilter
import com.shouwn.graduation.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true, // @Secured
        jsr250Enabled = true, // @RolesAllowed
        prePostEnabled = true // @PreAuthorize
)
class SecurityConfig @Autowired constructor(
        val customUserDetailsService: CustomUserDetailsService,
        val unauthorizedHandler: JwtAuthenticationEntryPoint
) : WebSecurityConfigurerAdapter(false){ // disableDefaults 가 뭐임?

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/res/**")
    }

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter = JwtAuthenticationFilter()

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(customUserDetailsService)
        super.configure(auth)
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()

    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable()
                .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy((SessionCreationPolicy.STATELESS))
                    .and()
                .authorizeRequests()
                    .antMatchers("/api/auth/**").permitAll()
                    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .anyRequest().authenticated()

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }
}
package com.shouwn.graduation.config

import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.service.MyAuthenticationProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class SecurityConfig @Autowired constructor(
        val myAuthenticationProvider: MyAuthenticationProvider
) : WebSecurityConfigurerAdapter(true){

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/res/**")
    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/user/login").permitAll()
                .antMatchers("/user").hasAuthority("ROLE_USER")
                .antMatchers("/guest").hasAuthority("ROLE_GUEST")
                .anyRequest().authenticated()
                .and()
    }
}
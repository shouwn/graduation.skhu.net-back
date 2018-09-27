package com.shouwn.graduation.config

import com.shouwn.graduation.model.domain.entity.User
import com.shouwn.graduation.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class SecurityConfig @Autowired constructor(
        val userService: UserService
//        val myAuthenticationProvider: MyAuthenticationProvider
) : WebSecurityConfigurerAdapter(false){ // disableDefaults 가 뭐임?

//    override fun configure(web: WebSecurity) {
//        web.ignoring().antMatchers("/res/**")
//    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable().authorizeRequests()
//                .antMatchers("/user/login").permitAll()
//                .antMatchers("/user").hasAuthority("ROLE_USER")
//                .antMatchers("/guest").hasAuthority("ROLE_GUEST")
                .anyRequest().authenticated()
                .and()
                .formLogin()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userService)
        super.configure(auth)
    }
}
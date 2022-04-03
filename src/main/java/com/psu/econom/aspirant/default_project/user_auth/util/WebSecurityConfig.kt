package com.psu.econom.aspirant.default_project.user_auth.util

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
open class WebSecurityConfig(
        private val jwtFilter: JwtFilter,
        private val exceptionHandler: AuthExceptionHandler
) : WebSecurityConfigurerAdapter() {

    public override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .mvcMatchers(*AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .logout().disable()
                .exceptionHandling()
                .authenticationEntryPoint(exceptionHandler).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)

    }

    companion object {
        val AUTH_WHITELIST = arrayOf(
                // -- swagger ui
                "/swagger-resources/**",
                "/v3/api-docs",
                "/swagger-ui/**",
                // -- user api
                "/auth/**",
        )
    }
}
package org.ryan.services.datasvc.engine.security

import org.ryan.services.datasvc.exception.{ForbiddenException, UnAuthorizedException}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler
import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
//@EnableReactiveMethodSecurity
class SecurityConfiguration {
  val log: Logger = LoggerFactory.getLogger(classOf[SecurityConfiguration])

  @Bean
  def securityWebFilterChain(http: ServerHttpSecurity,
                             authenticationManager: AuthenticationManager,
                             securityContextRepository: SecurityContextRepository): SecurityWebFilterChain = {
    http
      // It'll' respond text/plain if this exception handler was not defined.
      .exceptionHandling()
      .authenticationEntryPoint((_, _) =>
        Mono.error(new UnAuthorizedException)
      ).accessDeniedHandler((_, _) =>
      Mono.error(new ForbiddenException())
    ).and()
      .authorizeExchange()
      .pathMatchers(HttpMethod.OPTIONS).permitAll()
      .pathMatchers("/testsvc/**").permitAll()
      .pathMatchers("/api-docs/**").permitAll()
      .pathMatchers("/v3/**").permitAll()
      .pathMatchers(HttpMethod.GET, "/usersvc/v1/user/**").hasAnyRole("user", "admin")
      .pathMatchers(HttpMethod.POST, "/usersvc/v1/user/**").hasRole("admin")
      .anyExchange().authenticated()
      .and()
      .authenticationManager(authenticationManager)
      .securityContextRepository(securityContextRepository)
      .httpBasic().disable()
      .csrf().disable()
      .formLogin().disable()
      .logout().disable()
      .build()
  }
}

package org.ryan.services.datasvc.engine.security

import org.ryan.services.datasvc.common.JWT_TOKEN
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.{SecurityContext, SecurityContextImpl}
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class SecurityContextRepository(authenticationManager: AuthenticationManager) extends ServerSecurityContextRepository {
  val log: Logger = LoggerFactory.getLogger(classOf[SecurityContextRepository])

  override def save(exchange: ServerWebExchange, context: SecurityContext): Mono[Void] = {
    throw new UnsupportedOperationException("Not supported yet.")
  }

  override def load(exchange: ServerWebExchange): Mono[SecurityContext] = {
    log.debug("Authorization Header: " + Option(exchange.getRequest.getHeaders.getFirst(HttpHeaders.AUTHORIZATION)))
    Mono.justOrEmpty(exchange.getRequest.getHeaders.getFirst(HttpHeaders.AUTHORIZATION))
      .filter(authHeader => authHeader.startsWith(JWT_TOKEN.TOKEN_PREFIX))
      .flatMap(authHeader => {
        val authToken = authHeader.substring(7)

        // Default: isAuthenticated => false
        val authentication = new UsernamePasswordAuthenticationToken(authToken, authToken)

        this.authenticationManager.authenticate(authentication).map(authentication => {
          new SecurityContextImpl(authentication)
        })
      })
  }
}

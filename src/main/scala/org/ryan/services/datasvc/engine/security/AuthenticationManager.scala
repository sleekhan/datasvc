package org.ryan.services.datasvc.engine.security

import io.jsonwebtoken.Claims
import org.ryan.services.datasvc.common.JWT_TOKEN
import org.ryan.services.datasvc.service.JwtSignerService
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.security.authentication.{ReactiveAuthenticationManager, UsernamePasswordAuthenticationToken}
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

import java.util.stream.Collectors
import scala.jdk.CollectionConverters.SeqHasAsJava

@Component
class AuthenticationManager(jwtSignerService: JwtSignerService) extends ReactiveAuthenticationManager {
  val log: Logger = LoggerFactory.getLogger(classOf[AuthenticationManager])

  override def authenticate(authentication: Authentication): Mono[Authentication] = {
    val authToken = authentication.getCredentials.toString
    log.debug("authToken: " + authToken)

    val username = jwtSignerService.getUsernameFromToken(authToken)
    log.debug("username " + username)

    Mono.just(jwtSignerService.validateToken(authToken))
      .filter(valid => valid)
      .switchIfEmpty(Mono.empty())
      .map(_ => {
        val claims = jwtSignerService.getAllClaimsFromToken(authToken)
        val roleArray = getRoles(claims)

        roleArray match {
          case Some(x) =>
            val roles = x.stream().map(role => new SimpleGrantedAuthority(JWT_TOKEN.ROLE_PREFIX + role))
              .collect(Collectors.toList[SimpleGrantedAuthority])

            log.debug("role " + roles.toString)
            // Default: isAuthenticated => true
            new UsernamePasswordAuthenticationToken(username, "", roles)
          case None =>
            authentication
        }
      })
  }

  private def getRoles(claims: Claims): Option[java.util.List[String]] = {
    val roleArray = try {
      Right(Option(claims.get(JWT_TOKEN.ROLE_KEY_NAME, classOf[java.util.List[String]])))
    }
    catch {
      case e: Exception => Left(e)
    }
    roleArray match {
      case Right(x) => x
      case Left(_) =>
        val roleString = claims.get(JWT_TOKEN.ROLE_KEY_NAME, classOf[String])
        Option(roleString.split(",").toList.asJava)
    }
  }
}

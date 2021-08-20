package org.ryan.services.datasvc.service

import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.{Claims, Jwts}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import java.security.Key
import java.util.Date
import javax.annotation.PostConstruct

@Service
class JwtSignerService {
  val log: Logger = LoggerFactory.getLogger(classOf[JwtSignerService])

  @Value("${jjwt.secret}")
  val secret: String = null

  var key: Key = null

  @PostConstruct
  def init() = {
    log.debug("secret: " + secret)
    this.key = Keys.hmacShaKeyFor(secret.getBytes("utf-8"))
  }

  def getUsernameFromToken(token: String): String = {
    getAllClaimsFromToken(token).getSubject
  }

  def validateToken(token: String) = {
    !isTokenExpired(token)
  }

  private def isTokenExpired(token: String): Boolean = {
    val expiration = Option(getExpirationDateFromToken(token))
    expiration match {
      case Some(x) =>
        log.debug("expiration " + expiration.toString)
        x.before(new Date)
      case None => true
    }
  }

  def getExpirationDateFromToken(token: String): Date = {
    getAllClaimsFromToken(token).getExpiration
  }

  def getAllClaimsFromToken(token: String): Claims = {
    Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(token).getBody()
  }

}

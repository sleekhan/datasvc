package org.ryan.services.datasvc.data

import org.ryan.services.datasvc.model.User
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class UserRepository(redisTemplate: ReactiveRedisOperations[String, User]) {
  val log: Logger = LoggerFactory.getLogger(classOf[UserRepository])

  //  @PreAuthorize("hasRole('Admin')")
  def insertUser(user: User): Mono[java.lang.Long] = {
    redisTemplate.opsForList().leftPush(user.email, user)
  }

  //  @PreAuthorize("hasAnyRole('User', 'Admin')")
  def selectUser(email: String): Mono[User] = {
    redisTemplate.opsForList().index(email, 0)
  }

}

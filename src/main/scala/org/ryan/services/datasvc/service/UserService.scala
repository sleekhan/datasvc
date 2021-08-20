package org.ryan.services.datasvc.service

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.ryan.services.datasvc.data.UserRepository
import org.ryan.services.datasvc.exception.BadRequestException
import org.ryan.services.datasvc.model.{User, UserCreationResponse}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation._
import reactor.core.publisher.Mono

@RestController
@RequestMapping(Array("/usersvc"))
@Tag(name = "User Service")
@SecurityRequirement(name = "bearerAuth")
class UserController(userService: UserService) extends IUserController {
  val log: Logger = LoggerFactory.getLogger(classOf[UserController])

  def saveUser(user: User): Mono[ResponseEntity[UserCreationResponse]] = {
    log.debug("user: " + user.toString)
    val userOp = Option(user)

    userOp match {
      case Some(u) =>
        userService.createUser(u)
          .map(created => {
            val response = UserCreationResponse(created.toInt)
            ResponseEntity.status(HttpStatus.CREATED).body(response)
          })

      case None =>
        Mono.error(new BadRequestException())
    }
  }

  def retrieveUser(email: String): Mono[ResponseEntity[User]] = {
    log.debug(email)
    val emailOp = Option(email)

    emailOp match {
      case Some(email) =>
        userService.findUser(email)
          .map(user => ResponseEntity.ok().body(user))
          // There is no benefit to raise NoContext Exception because the response won't has any body content
          .switchIfEmpty(Mono.just(ResponseEntity.noContent().build()))
      case None =>
        Mono.error(new BadRequestException())
    }
  }
}

@Service
//@PreAuthorize("hasAnyRole('User', 'Admin')")
class UserService(userRepository: UserRepository) {
  val log: Logger = LoggerFactory.getLogger(classOf[UserService])

  def createUser(user: User): Mono[java.lang.Long] = {
    userRepository.insertUser(user)
  }

  //  @PreAuthorize("hasAnyRole('User', 'Admin')")
  def findUser(email: String): Mono[User] = {
    userRepository.selectUser(email)
  }
}

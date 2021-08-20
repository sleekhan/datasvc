package org.ryan.services.datasvc.service

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.ryan.services.datasvc.model.{User, UserCreationResponse}
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.{GetMapping, PathVariable, PostMapping, RequestBody}
import reactor.core.publisher.Mono

import javax.validation.Valid

trait IUserController {
  // -----------------------------------------------------------------------------------------------
  @Operation(summary = "Save a user", responses = Array(
    new ApiResponse(description = "Successful Operation",
      responseCode = "201",
      content = Array(new Content(mediaType = "application/json", schema = new Schema(implementation = classOf[UserCreationResponse])))),
    new ApiResponse(description = "Unauthorized",
      responseCode = "401",
      content = Array(new Content(mediaType = "application/json"))),
    new ApiResponse(description = "Forbidden",
      responseCode = "403",
      content = Array(new Content(mediaType = "application/json")))
  ))
  @PostMapping(Array("/v1/user"))
  def saveUser(@Valid
               @RequestBody user: User): Mono[ResponseEntity[UserCreationResponse]]

  // -----------------------------------------------------------------------------------------------
  @Operation(summary = "Retrieve a user information with the email", responses = Array(
    new ApiResponse(description = "Successful Operation",
      responseCode = "200",
      content = Array(new Content(mediaType = "application/json", schema = new Schema(implementation = classOf[User])))),
    new ApiResponse(description = "No user to have the email",
      responseCode = "404",
      content = Array(new Content(mediaType = "application/json"))),
    new ApiResponse(description = "Unauthorized",
      responseCode = "401",
      content = Array(new Content(mediaType = "application/json"))),
    new ApiResponse(description = "Forbidden",
      responseCode = "403",
      content = Array(new Content(mediaType = "application/json")))
  ))
  @GetMapping(Array("/v1/user/{email}"))
  def retrieveUser(@PathVariable email: String): Mono[ResponseEntity[User]]
}
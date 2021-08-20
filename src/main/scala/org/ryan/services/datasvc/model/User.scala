package org.ryan.services.datasvc.model

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
import com.fasterxml.jackson.annotation.{JsonAutoDetect, JsonProperty}
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.swagger.v3.oas.annotations.media.Schema

import javax.validation.constraints.{Email, NotBlank}

@Schema(description = "User Information")
@JsonSerialize
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
case class User() {
  @Email
  @NotBlank
  @JsonProperty
  val email: String = ""

  @JsonProperty
  val name: String = ""
}
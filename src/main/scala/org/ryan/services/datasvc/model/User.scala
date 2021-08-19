package org.ryan.services.datasvc.model

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
import com.fasterxml.jackson.annotation.{JsonAutoDetect, JsonProperty}
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "User Information")
@JsonSerialize
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
case class User(@JsonProperty("email") email: String, @JsonProperty("name") name: String)
package org.ryan.services.datasvc.model

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
import com.fasterxml.jackson.annotation.{JsonAutoDetect, JsonProperty}
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
case class UserCreationResponse(@JsonProperty("created") created: Int)

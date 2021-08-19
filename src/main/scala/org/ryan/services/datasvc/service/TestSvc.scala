package org.ryan.services.datasvc.service

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
import com.fasterxml.jackson.annotation.{JsonAutoDetect, JsonProperty}
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RestController}
import reactor.core.publisher.{Flux, Mono}

@JsonSerialize
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
case class DTO(
                @JsonProperty
                messasge: String)

@RestController
@RequestMapping(Array("/testsvc"))
@Tag(name = "Test Service")
class TestController(testSvc: TestSvc) {
  @GetMapping(Array("/echo"))
  def echo(response: ServerHttpResponse): Mono[DTO] = {
    testSvc.echoMessage()
  }

  @GetMapping(Array("/echo1"))
  def echo1(): Flux[DTO] = {
    testSvc.echoMessage1()
  }
}

@Service
class TestSvc {
  def echoMessage(): Mono[DTO] = {
    val dto = DTO("This is the first message.")
    Mono.just(dto)
  }

  def echoMessage1(): Flux[DTO] = {
    val dto1 = DTO("This is the first message.")
    val dto2 = DTO("This is the second message.")

    Flux.just(dto1, dto2)
  }
}

//val a = Semigroup[Int].combine(1, 2)
//val b = Semigroup[List[Int]].combine(List(1,2,3), List(4,5,6))
//val c = Semigroup[Option[Int]].combine(Option(1), Option(2))
//val d = Semigroup[Option[Int]].combine(Option(1), None)
//println(a)
//println(b)
//println(c)
//println(d)

package org.ryan.services.datasvc.service

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
import com.fasterxml.jackson.annotation.{JsonAutoDetect, JsonProperty}
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.{GetMapping, RequestMapping, RestController}
import org.springframework.web.reactive.function.client.WebClient
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
  val log: Logger = LoggerFactory.getLogger(classOf[TestController])

  @GetMapping(Array("/echo"))
  def echo(response: ServerHttpResponse): Mono[DTO] = {
    testSvc.echoMessage()
  }

  @GetMapping(Array("/echo1"))
  def echo1(): Flux[DTO] = {
    testSvc.echoMessage1()
  }

  @GetMapping(Array("/data"))
  def success(key: String): String = {
    log.debug("key: " + key)
    if (key == "fail") throw new RuntimeException
    "data"
  }

  @GetMapping(Array("/annotation"))
  def testAnnotation(key: String): Mono[String] = testSvc.getApiData(key)
}

@Service
class TestSvc(webClientBuilder: WebClient.Builder) {
  val log: Logger = LoggerFactory.getLogger(classOf[TestSvc])

  def echoMessage(): Mono[DTO] = {
    val dto = DTO("This is the first message.")
    Mono.just(dto)
  }

  def echoMessage1(): Flux[DTO] = {
    val dto1 = DTO("This is the first message.")
    val dto2 = DTO("This is the second message.")

    Flux.just(dto1, dto2)
  }

  @CircuitBreaker(name = "test", fallbackMethod = "fallback")
  def getApiData(key: String): Mono[String] = {
    log.debug("key1: " + key)
    val client = webClientBuilder.baseUrl("http://localhost:8080").build()
    client.get().uri(uriBuilder => uriBuilder.path("/testsvc/data").queryParam("key", key).build(key))
      .retrieve()
      .bodyToMono(classOf[String])
  }

  def fallback(key: String, t: Throwable) = {
    Mono.just("fallback data")
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

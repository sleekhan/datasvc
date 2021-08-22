package org.ryan.services.datasvc

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.circuitbreaker.resilience4j.{ReactiveResilience4JCircuitBreakerFactory, Resilience4JConfigBuilder}
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient

import java.time.Duration

@SpringBootApplication
class DatasvcApplication

object DatasvcApplication {
  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[DatasvcApplication], args: _*)
  }

  @Bean
  def webClientBuilder(): WebClient.Builder = {
    WebClient.builder()
  }

  @Bean
  def defaultCustomizer(): Customizer[ReactiveResilience4JCircuitBreakerFactory] = {
    factory =>
      factory.configureDefault(id => new Resilience4JConfigBuilder(id)
        .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
        .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(3)).build()).build())
  }
}




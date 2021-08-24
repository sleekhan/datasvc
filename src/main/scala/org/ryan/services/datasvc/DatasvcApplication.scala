package org.ryan.services.datasvc

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient

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
}




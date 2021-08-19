package org.ryan.services.datasvc

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.{SecurityRequirement, SecurityScheme}
import io.swagger.v3.oas.models.{Components, OpenAPI}
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

import java.lang.annotation.Annotation


@SpringBootApplication
class DatasvcApplication

object DatasvcApplication {
  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[DatasvcApplication], args: _*)
  }
}




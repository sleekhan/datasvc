package org.ryan.services.datasvc

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
class DatasvcApplication

object DatasvcApplication {
  def main(args: Array[String]): Unit = {
    SpringApplication.run(classOf[DatasvcApplication], args: _*)
  }
}




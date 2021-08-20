package org.ryan.services.datasvc.engine

import org.springframework.boot.autoconfigure.web.WebProperties.Resources
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.context.support.DefaultMessageSourceResolvable
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server._

import java.util.stream.Collectors

@Component
@Order(-2)
class DefaultApplicationErrorWebExceptionHandler(
                                                  errorAttributes: ErrorAttributes,
                                                  resources: Resources,
                                                  applicationContext: ApplicationContext,
                                                  configurer: ServerCodecConfigurer)
  extends AbstractErrorWebExceptionHandler(errorAttributes, resources, applicationContext) {
  this.setMessageWriters(configurer.getWriters)

  protected def getRoutingFunction(errorAttributes: ErrorAttributes): RouterFunction[ServerResponse] =
    RouterFunctions.route(RequestPredicates.all, this.renderErrorResponse)

  private def renderErrorResponse(request: ServerRequest) = {
    val errorPropertiesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults)

    val validationError = getError(request)
    if (validationError.isInstanceOf[WebExchangeBindException]) {
      val errors = validationError.asInstanceOf[WebExchangeBindException]
        .getAllErrors.stream().map(e => new DefaultMessageSourceResolvable(e).getDefaultMessage)
        .collect(Collectors.toList[String])
      errorPropertiesMap.put("message", errors)
    }

    val errorCode = errorPropertiesMap.get("status").asInstanceOf[Int]
    ServerResponse.status(errorCode).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(errorPropertiesMap))
  }
}

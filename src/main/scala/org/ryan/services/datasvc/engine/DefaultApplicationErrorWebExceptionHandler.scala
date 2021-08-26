package org.ryan.services.datasvc.engine

import org.ryan.services.datasvc.common.EXCEPTION_MSG
import org.springframework.boot.autoconfigure.web.WebProperties.Resources
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.context.support.DefaultMessageSourceResolvable
import org.springframework.core.annotation.Order
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.{HttpStatus, MediaType}
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server._

import java.util.stream.Collectors
import javax.validation.ConstraintViolationException

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

    validationError match {
      case ve: WebExchangeBindException =>
        val errors = ve
          .getAllErrors.stream().map(e => new DefaultMessageSourceResolvable(e).getDefaultMessage)
          .collect(Collectors.toList[String])
        errorPropertiesMap.put(EXCEPTION_MSG.MESSAGE_KEY, errors)
      case cve: ConstraintViolationException =>
        val rawMessage = validationError.getMessage.split(":")
        val message = if (rawMessage.length >= 2) Array(rawMessage(1).trim) else rawMessage
        errorPropertiesMap.put(EXCEPTION_MSG.MESSAGE_KEY, message)
        errorPropertiesMap.put("status", HttpStatus.BAD_REQUEST.value())
        errorPropertiesMap.put("error", "Bad Request")
      case _ =>
        errorPropertiesMap.put(EXCEPTION_MSG.MESSAGE_KEY, null)
    }

    val errorCode = errorPropertiesMap.get("status").asInstanceOf[Int]
    ServerResponse.status(errorCode).contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(errorPropertiesMap))
  }
}
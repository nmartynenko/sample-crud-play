package controllers

import exceptions.{GlossaryException, NoGlossaryFoundException}
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.{ResponseStatus, ExceptionHandler, ResponseBody, ControllerAdvice}
import play.api.Logger
import play.api.i18n.Messages

@ControllerAdvice
class GlossaryControllerAdvice extends BaseController {

  //EXCEPTION HANDLERS
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = Array(classOf[NoGlossaryFoundException]))
  @ResponseBody
  def handleNoGlossaryFoundException(e: NoGlossaryFoundException) = {
    Messages("sample.error.glossary.not.found", e.getModelId)
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = Array(classOf[GlossaryException]))
  @ResponseBody
  def handleGlossaryException(e: GlossaryException) = {
    simpleExceptionHandler(e)
  }

  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(value = Array(classOf[AccessDeniedException]))
  @ResponseBody
  def handleAccessDeniedException(e: AccessDeniedException) {
    simpleExceptionHandler(e)
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(value = Array(classOf[AuthenticationException]))
  @ResponseBody
  def handleAuthenticationException(e: AuthenticationException) = {
    simpleExceptionHandler(e)
  }

  //logs exception and returns it's message
  protected def simpleExceptionHandler(th: Throwable) = {
    Logger.error(th.getMessage, th)

    th.getMessage
  }

}

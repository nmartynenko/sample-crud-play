package controllers

import com.aimprosoft.play.glossaries.exceptions.{GlossaryException, NoGlossaryFoundException}
import com.fasterxml.jackson.core.JsonProcessingException
import org.springframework.http.HttpStatus
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

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = Array(classOf[JsonProcessingException]))
  @ResponseBody
  def handleAuthenticationException(e: JsonProcessingException) = {
    e.getMessage
  }

  //logs exception and returns it's message
  protected def simpleExceptionHandler(th: Throwable) = {
    Logger.error(th.getMessage, th)

    th.getMessage
  }

}

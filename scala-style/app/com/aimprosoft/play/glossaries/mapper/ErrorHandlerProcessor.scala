package com.aimprosoft.play.glossaries.mapper

import com.aimprosoft.play.glossaries.exceptions.{GlossaryException, NoGlossaryFoundException}
import play.api.Logger
import play.api.i18n.Messages
import play.api.mvc.Results._
import play.api.mvc._
import scala.concurrent.Future

trait ErrorHandler[-T <: Throwable] {
  def handleError(ex: T, request: RequestHeader): Option[Result]
}

class SimpleErrorHandler extends ErrorHandler[Exception]{
  def handleError(ex: Exception, request: RequestHeader): Option[Result] = {
    Logger.error(ex.getMessage, ex)

    Some(InternalServerError(ex.getMessage))
  }
}

class NoGlossaryFoundErrorHandler extends ErrorHandler[NoGlossaryFoundException]{
  def handleError(ex: NoGlossaryFoundException, request: RequestHeader): Option[Result] = {
    Some(BadRequest(Messages("sample.error.glossary.not.found", ex.modelId)))
  }
}

/**
 * More advanced error handler processor
 * might be useful, if exception handlers are scanned on application startup
 *
 * Note: currently it's not used in favor of simple pattern matching
 */
object ErrorHandlerProcessor {

  val exactMatch: Boolean = false

  private val exceptionHandlers: Map[Class[_ <: Throwable], ErrorHandler[_ <: Throwable]] = Map(
    classOf[GlossaryException] -> new NoGlossaryFoundErrorHandler,
    classOf[Exception] -> new SimpleErrorHandler
  )

  def handleError(ex: Throwable, request: RequestHeader): Option[Future[Result]] = {
    def findMatch(matchClass: Class[_ <: Throwable]): Option[ErrorHandler[_ <: Throwable]] = {
      exceptionHandlers.get(matchClass) match {
        case ret @ Some(_) =>
          ret
        case None if exactMatch =>
          None
        case _ =>
          //try to find some other
          matchClass.getSuperclass match {
            case sp if classOf[Throwable].isAssignableFrom(sp) =>
              findMatch(sp.asInstanceOf[Class[Throwable]])
            case _ =>
              None
          }
      }
    }

    val handlerOption = findMatch(ex.getClass)

    handlerOption match {
      case Some(handler: ErrorHandler[Throwable]) =>
        handler.handleError(ex, request) match {
          case Some(result) =>
            Some(Future.successful(result))
          case _ =>
            None
        }
      case _ =>
        None
    }
  }

}
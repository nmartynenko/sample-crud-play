package mapper

import exceptions.{GlossaryException, NoGlossaryFoundException}
import play.api.i18n.Messages
import play.api.mvc.Results._
import play.api.mvc._
import scala.concurrent.Future
import play.api.Logger

trait ErrorHandler[T <: Throwable] {
  def handleError(ex: T, request: RequestHeader): Option[SimpleResult]
}

/**
 * More advanced error handler processor
 * might be useful, if exception handlers are scanned on application startup
 *
 * Note: currently it's not used in favor of simple pattern matching
 */
class SimpleErrorHandler extends ErrorHandler[Exception]{
  def handleError(ex: Exception, request: RequestHeader): Option[SimpleResult] = {
    Logger.error(ex.getMessage, ex)

    Some(InternalServerError(ex.getMessage))
  }
}

class NoGlossaryFoundErrorHandler extends ErrorHandler[NoGlossaryFoundException]{
  def handleError(ex: NoGlossaryFoundException, request: RequestHeader): Option[SimpleResult] = {
    Some(BadRequest(Messages("sample.error.glossary.not.found", ex.modelId)))
  }
}

trait ErrorHandlerProcessor {

  val exactMatch: Boolean = false

  private val exceptionHandlers: Map[Class[_ <: Throwable], ErrorHandler[_ <: Throwable]] = Map(
    classOf[GlossaryException] -> new NoGlossaryFoundErrorHandler,
    classOf[Exception] -> new SimpleErrorHandler
  )

  def handleError[T <: Throwable](ex: T, request: RequestHeader): Option[Future[SimpleResult]] = {
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
      case Some(handler) =>
        //note: double check what is wrong with this generic type
        handler.asInstanceOf[ErrorHandler[Throwable]].handleError(ex, request) match {
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
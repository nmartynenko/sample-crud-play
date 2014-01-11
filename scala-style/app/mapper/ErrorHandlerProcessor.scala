package mapper

import play.api.mvc.Results._
import play.api.mvc._
import scala.concurrent.Future

trait ErrorHandler {
  def handleError(ex: Throwable, request: RequestHeader): Option[SimpleResult]
}

trait ErrorHandlerProcessor {

  val exactMatch: Boolean = false

  private val exceptionHandlers: Map[Class[_ <: Throwable], ErrorHandler] = Map()

  def handleError(ex: Throwable, request: RequestHeader): Option[Future[SimpleResult]] = {
    def findMatch(matchClass: Class[_ <: Throwable]): Option[ErrorHandler] = {
      exceptionHandlers.get(matchClass) match {
        case ret @ Some(_) =>
          ret
        case None if !exactMatch =>
          None
        case _ =>
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
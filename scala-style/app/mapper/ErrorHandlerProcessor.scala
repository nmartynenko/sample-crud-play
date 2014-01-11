package mapper

import play.api.mvc.Results._
import play.api.mvc._
import scala.concurrent.Future

trait ErrorHandlerProcessor {

  val exactMatch: Boolean = false

  private val exceptionHandlers: Map[Class[_ <: Throwable], ErrorHandler] = Map()

  def handleError(ex: Throwable, request: RequestHeader): Option[Future[SimpleResult]] = {
    def findMatch(exClass: Class[_ <: Throwable]): Option[ErrorHandler] = {
      None
    }

    val exClass = ex.getClass

    //todo
    None
  }

}

trait ErrorHandler {
  def handleError(ex: Throwable, request: RequestHeader): Option[SimpleResult]
}
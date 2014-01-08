import play.api.mvc._
import play.api.mvc.Results._
import scala.concurrent.Future

trait ErrorHandlerProcessor {

  //todo
  def handleError(request: RequestHeader, ex: Throwable): Option[Future[SimpleResult]] = {
    None
  }

}

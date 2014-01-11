import mapper.ErrorHandlerProcessor
import play.api.mvc.Results._
import play.api.mvc._
import play.api.{Logger, Application, GlobalSettings}
import listeners._
import scala.concurrent.Future

object Global extends GlobalSettings with ErrorHandlerProcessor{

  override def onStart(app: Application): Unit = {
    Logger.info("Pre-fill some data")

    //it might be better to improve scanning
    val listeners = List(ApplicationDDLCreator, CreateAdminListener, CreateUserListener, CreateGlossaryDataListener)
    for(listener <- listeners){
      listener.init()
    }

    Logger.info("Initialization has ended")
  }

  override def onHandlerNotFound(request: RequestHeader): Future[SimpleResult] = {
    Future.successful(NotFound(views.html.page404()))
  }

  override def onError(request: RequestHeader, ex: Throwable): Future[SimpleResult] = {
    val handler = handleError(ex, request)

    handler.getOrElse(super.onError(request, ex))
  }

}

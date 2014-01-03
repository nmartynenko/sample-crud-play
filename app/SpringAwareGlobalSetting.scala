import org.springframework.context._
import org.springframework.context.support._
import play.api._
import play.api.mvc.Results._
import play.api.mvc.SimpleResult
import scala.concurrent.Future

object SpringAwareGlobalSetting extends GlobalSettings with ControllerAdviceProcessor{

  private var ctx: ConfigurableApplicationContext = null

  override def onStart(app: Application) {
    Logger.info("Initialize Spring context for Play application")
    ctx = new ClassPathXmlApplicationContext("spring/*.xml")

    Logger.info("Initialization is complete")

    initControllerAdvice(ctx)
  }

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    ctx.getBean(controllerClass)
  }

  override def onStop(app: Application): Unit = {
    ctx.close()
  }

  override def onHandlerNotFound(request: mvc.RequestHeader): Future[SimpleResult] = {
    Future.successful(NotFound(views.html.page404()))
  }

  override def onError(request: mvc.RequestHeader, ex: Throwable): Future[SimpleResult] = {
    val handler = handleError(request, ex)

    handler.getOrElse(super.onError(request, ex))
  }

  override def onBadRequest(request: mvc.RequestHeader, error: String): Future[SimpleResult] = super.onBadRequest(request, error)
}
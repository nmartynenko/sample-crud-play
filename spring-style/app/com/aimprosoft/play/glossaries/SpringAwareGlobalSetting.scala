package com.aimprosoft.play.glossaries

import org.springframework.beans.factory.BeanFactoryUtils
import org.springframework.context.support._
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import play.api._
import play.api.mvc.Results._
import play.api.mvc.{Action, Handler, RequestHeader, SimpleResult}
import scala.concurrent.Future

object SpringAwareGlobalSetting extends GlobalSettings
  with ControllerAdviceProcessor with SecurityInterceptor {

  private var ctx: AbstractXmlApplicationContext = null

  override def onStart(app: Application) {
    Logger.info("Initialize Spring context for Play application")
    //create basic context
    val basiCtx = new ClassPathXmlApplicationContext("spring/*.xml")

    Logger.info("Initialization is complete")

    //allow to proxy controllers with security annotations
    ctx = new ClassPathXmlApplicationContext(basiCtx)

    //set config locations
    ctx.setConfigLocation("controllers/spring-security-controllers.xml")

    //refresh context
    ctx.refresh()

    //init controller advices
    initControllerAdvice(ctx)
  }

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    BeanFactoryUtils.beanOfTypeIncludingAncestors(ctx, controllerClass)
  }

  override def onStop(app: Application): Unit = {
    ctx.close()
  }

  override def onRouteRequest(request: RequestHeader): Option[Handler] = {
    setAuth(request)

    try {
      super.onRouteRequest(request)
    }
    catch {
      //handle only security exceptions
      case e: AuthenticationException =>
        Some(Action {
          Redirect("/login.html")
        })
      case e: AccessDeniedException =>
        Some(Action {
          Unauthorized(views.html.defaultpages.unauthorized())
        })
    }
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
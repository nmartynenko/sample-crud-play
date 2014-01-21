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

  private var ctx: AbstractApplicationContext = _

  override def onStart(app: Application) {
    Logger.info("Initialize Spring context for Play application")

    val basicCtx = Play.current.configuration.getString("spring.context") match {
      //there should be some spring context
      case Some(loc) =>
        new GenericXmlApplicationContext(loc)
      //otherwise fail an error
      case _ =>
        sys.error("There is no Spring locations configured")
    }

    ctx = Play.current.configuration.getString("spring.additional.context") match {
      //check whether there are additional configs
      case Some(loc) =>
        //create new context
        val addCtx = new GenericXmlApplicationContext()

        //allow to proxy controllers with security annotations
        addCtx.setParent(basicCtx)

        //set config locations
        addCtx.load(loc)

        //refresh context
        addCtx.refresh()

        addCtx
      //otherwise basic context is the main context
      case _ =>
        basicCtx
    }

    Logger.info("Initialization is complete")

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
    //ignore static assets
    if (!(request.path startsWith "/assets")){
      setAuth(request)
    }

    try {
      super.onRouteRequest(request)
    }
    catch {
      //handle only security exceptions
      case ex: AuthenticationException =>
        Some(Action {
          Redirect("/login.html")
        })
      case ex: AccessDeniedException =>
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
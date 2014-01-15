package com.aimprosoft.play.glossaries

import _root_.listeners.{CreateGlossaryDataListener, CreateUserListener, CreateAdminListener, ApplicationDDLCreator}
import _root_.mapper.ErrorHandlerProcessor
import _root_.mapper.ErrorResponses._
import listeners._
import play.api.mvc.Results._
import play.api.mvc.SimpleResult
import play.api.mvc._
import play.api.{Logger, Application, GlobalSettings}
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

    //onError original exceptions are always wrapped with Play ones
    ex.getCause match {
      case e: Exception => handleException(e, request)
      case _ =>
        super.onError(request, ex)
    }
  }

}

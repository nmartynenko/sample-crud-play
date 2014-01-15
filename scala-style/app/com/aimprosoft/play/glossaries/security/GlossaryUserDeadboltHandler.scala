package com.aimprosoft.play.glossaries.security

import be.objectify.deadbolt.core.models.Subject
import be.objectify.deadbolt.scala.{DynamicResourceHandler, DeadboltHandler}
import play.api.Logger
import play.api.Play.current
import play.api.cache.Cache
import play.api.mvc.Results._
import play.api.mvc.{Security, SimpleResult, Request}
import scala.concurrent.Future

trait GlossaryUserDeadboltHandler extends DeadboltHandler {

  protected val redirect: SimpleResult

  protected val handlerOption = Some(GlossaryDynamicResourceHandler)

  def getSubject[A](request: Request[A]): Option[Subject] = {
    //get username from session
    request.session.get(Security.username) match {
      case None =>
        None
      case Some(username) =>
        val fromCache = Cache.getAs[Subject](username)

        Logger.debug(s"Getting value for $username, and it returns $fromCache")

        fromCache
    }
  }

  def onAuthFailure[A](request: Request[A]): Future[SimpleResult] = Future.successful {
    Forbidden
  }

  def getDynamicResourceHandler[A](request: Request[A]): Option[DynamicResourceHandler] = {
    handlerOption
  }
}

object SubjectPresentGlossaryUserDeadboltHandler extends GlossaryUserDeadboltHandler{

  protected val redirect: SimpleResult = Redirect("/login.html")

  def beforeAuthCheck[A](request: Request[A]): Option[Future[SimpleResult]] = {
    getSubject(request) match {
      case None =>
        //redirect to login page if there is no subject
        Some(Future.successful(redirect))
      case _ =>
        None
    }
  }

}

object SubjectNotPresentGlossaryUserDeadboltHandler extends GlossaryUserDeadboltHandler{

  protected val redirect: SimpleResult = Redirect("/index.html")

  def beforeAuthCheck[A](request: Request[A]): Option[Future[SimpleResult]] = {
    getSubject(request) match {
      case None =>
        None
      case _ =>
        //redirect to home page if there is no subject
        Some(Future.successful(redirect))
    }
  }

}

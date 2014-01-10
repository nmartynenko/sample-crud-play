package security

import be.objectify.deadbolt.core.models.Subject
import be.objectify.deadbolt.scala.{DynamicResourceHandler, DeadboltHandler}
import play.api.mvc.Results._
import play.api.Play.current
import play.api.mvc.{Security, SimpleResult, Request}
import scala.concurrent.Future
import play.api.cache.Cache

object GlossaryUserDeadboltHandler extends DeadboltHandler {

  private val loginPage = Redirect("/login.html")

  private val handlerOption = Some(GlossaryDynamicResourceHandler)

  def beforeAuthCheck[A](request: Request[A]): Option[Future[SimpleResult]] = {
    //get username from session
    request.session.get(Security.username) match {
      //redirect to login page if there is no username
      case None =>
        Some(Future.successful(loginPage))
      //validation of username will happen a bit later
      case _ =>
        None
    }
  }

  def getSubject[A](request: Request[A]): Option[Subject] = {
    //session should be present
    val username = request.session(Security.username)

    //todo find out why this doesn't work in reality
    Cache.getAs[Subject](username)
  }

  def onAuthFailure[A](request: Request[A]): Future[SimpleResult] = Future.successful {
    Forbidden
  }

  def getDynamicResourceHandler[A](request: Request[A]): Option[DynamicResourceHandler] = {
    handlerOption
  }
}

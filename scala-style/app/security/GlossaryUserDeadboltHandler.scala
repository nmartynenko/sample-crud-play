package security

import be.objectify.deadbolt.core.models.Subject
import be.objectify.deadbolt.scala.{DynamicResourceHandler, DeadboltHandler}
import play.api.mvc.Results._
import play.api.Play.current
import play.api.mvc.{Security, SimpleResult, Request}
import scala.concurrent.Future
import play.api.cache.Cache
import play.api.Logger

object GlossaryUserDeadboltHandler extends DeadboltHandler {

  private val loginPage = Redirect("/login.html")

  private val handlerOption = Some(GlossaryDynamicResourceHandler)

  def beforeAuthCheck[A](request: Request[A]): Option[Future[SimpleResult]] = {
    def login = Some(Future.successful(loginPage))

    //get username from session
    request.session.get(Security.username) match {
      //redirect to login page if there is no username
      case None =>
        login
      case Some(username) =>
        //validate username
        Cache.get(username) match {
          case None =>
            login
          case _ =>
            None
        }
    }
  }

  def getSubject[A](request: Request[A]): Option[Subject] = {
    //session should be present
    val username = request.session(Security.username)

    val fromCache = Cache.getAs[Subject](username)

    Logger.debug(s"Getting value for $username, and it returns $fromCache")

    fromCache
  }

  def onAuthFailure[A](request: Request[A]): Future[SimpleResult] = Future.successful {
    Forbidden
  }

  def getDynamicResourceHandler[A](request: Request[A]): Option[DynamicResourceHandler] = {
    handlerOption
  }
}

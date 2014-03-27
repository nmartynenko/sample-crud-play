package controllers

import com.aimprosoft.play.glossaries.security.{GlossaryUserSubject, SecurityUserService}
import play.api.Logger
import play.api.Play.current
import play.api.cache.Cache
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

object LoginController extends SecuredController {

  //define login form
  val loginForm = Form(
    tuple(
      "j_username" -> text,
      "j_password" -> text
    ) verifying (error = "Invalid email or password", constraint = {
      case (username, password) =>
        check(username, password)
      case _ =>
        false
    })
  )

  private def check(username: String, password: String) = {
    val auth = SecurityUserService.authenticate(username, password)

    if (auth.isEmpty) false
    else {
      val identifier = GlossaryUserSubject.generateIdentifier(username)

      Logger.debug(s"Put value $identifier for $username")

      //put auth in the session
      Cache.set(identifier, auth.get)

      true
    }
  }

  def login = notAuthenticated {
    Action { implicit request =>
      Ok(views.html.login(loginForm))
    }
  }


  def authenticate = notAuthenticated {
    Action { implicit request =>
      loginForm.bindFromRequest.fold(
        formWithErrors =>
          BadRequest(views.html.login(formWithErrors)),

        form =>
          HOME.withSession(Security.username -> GlossaryUserSubject.generateIdentifier(form._1))
      )
    }
  }

  def logout = Action {
    Redirect(routes.LoginController.login()).withNewSession.flashing(
      "success" -> "You are now logged out."
    )
  }

}

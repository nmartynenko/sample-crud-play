package controllers

import play.api.Play.current
import play.api.cache.Cache
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import security.{GlossaryUserSubject, SecurityUserService}

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

  def check(username: String, password: String) = {
    val auth = SecurityUserService.authenticate(username, password)

    if (auth.isEmpty) false
    else {
      //put auth in the session
      Cache.set(auth.get.getIdentifier, auth)

      true
    }
  }

  def login = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors =>
        BadRequest(views.html.login(formWithErrors)),

      form =>
        HOME.withSession(Security.username -> GlossaryUserSubject.generateIdentifier(form._1))
    )
  }

  def logout = Action {
    Redirect(routes.LoginController.login).withNewSession.flashing(
      "success" -> "You are now logged out."
    )
  }

}

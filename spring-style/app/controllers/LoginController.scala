package controllers

import com.aimprosoft.play.glossaries.security.GlossaryAuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import play.api.Logger
import play.api.Play.current
import play.api.cache.Cache
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

@MVCController
class LoginController extends BaseController{

  @Autowired
  private val authService: GlossaryAuthenticationService = null

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
    val auth = authService.authenticate(username, password)

    if (auth.isEmpty) false
    else {
      val identifier = authService.generateIdentifier(username)

      Logger.debug(s"Put value $identifier for $username")

      //put auth in the session
      Cache.set(identifier, auth.get)

      true
    }
  }

  def login = {
    Action { implicit request =>
      Ok(views.html.login(loginForm))
    }
  }


  def authenticate = {
    Action { implicit request =>
      loginForm.bindFromRequest.fold(
        formWithErrors =>
          BadRequest(views.html.login(formWithErrors)),

        form =>
          HOME.withSession(Security.username -> authService.generateIdentifier(form._1))
      )
    }
  }

  def logout = Action {
    Redirect("/login.html").withNewSession.flashing(
      "success" -> "You are now logged out."
    )
  }

}

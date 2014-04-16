package controllers

import play.api.i18n.Lang
import play.api.mvc._
import org.springframework.security.access.prepost.PreAuthorize

@MVCController
@PreAuthorize("isAuthenticated()")
class HtmlController extends Controller{
  private val HOME = Redirect("/index.html")

  def home() = Action {HOME}

  def index(lang: String = "en") = Action {implicit request =>
    implicit val language = Lang(lang)

    Ok(views.html.index())
  }

}
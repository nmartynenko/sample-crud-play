package controllers

import play.api.i18n.Lang
import play.api.mvc._

object HtmlController extends SecuredController {
  private val HOME = Redirect("/index.html")

  def home() = Action {HOME}

  def index(lang: String = "en") = Action {request =>
    Ok(views.html.index()(Lang(lang)))
  }

}
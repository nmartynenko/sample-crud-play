package controllers

import play.api.i18n.Lang
import play.api.mvc._

@MVCController
class HtmlController extends Controller{
  private val HOME = Redirect("/index.html")

  def home() = Action {HOME}

  def index(lang: String = "en") = Action {request =>
    Ok(views.html.index()(Lang(lang)))
  }

}
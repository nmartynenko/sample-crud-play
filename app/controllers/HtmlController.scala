package controllers

import play.api.mvc._
import play.api.i18n.Lang
import play.api.Play.current

@MVCController
class HtmlController extends Controller{
  private val HOME = Redirect("/index.html")

  def home() = Action {HOME}

  def index() = Action {
    Ok(views.html.index())
  }

  def changeLocale(lang: String = "en") = Action {
    HOME.withLang(Lang(lang))
  }
}
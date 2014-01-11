package controllers

import play.api.Play.current
import play.api.i18n.Lang
import play.api.mvc._
import security.SubjectPresentGlossaryUserDeadboltHandler

object HtmlController extends SecuredController {
  def home() = Action {HOME}

  def index(lang: String = "en") = authenticated {
    Action {implicit request =>
      Ok(
        views.html.index(SubjectPresentGlossaryUserDeadboltHandler)
      ).withLang(Lang(lang))
    }
  }

}
package controllers

import play.api.i18n.Lang
import play.api.mvc._
import security.GlossaryUserDeadboltHandler

object HtmlController extends SecuredController {
  def home() = Action {HOME}

  def index(lang: String = "en") = SubjectPresent(GlossaryUserDeadboltHandler) {
    Action {request =>
      Ok(views.html.index()(Lang(lang)))
    }
  }

}
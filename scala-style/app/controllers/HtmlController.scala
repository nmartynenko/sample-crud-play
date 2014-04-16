package controllers

import com.aimprosoft.play.glossaries.security.SubjectPresentGlossaryUserDeadboltHandler
import play.api.i18n.Lang
import play.api.mvc._

object HtmlController extends SecuredController {

  def home() = Action {HOME}

  def index(lang: String = "en") = authenticated {
    Action {implicit request =>
      implicit val language = Lang(lang)

      Ok(
        views.html.index(SubjectPresentGlossaryUserDeadboltHandler)
      )
    }
  }

}
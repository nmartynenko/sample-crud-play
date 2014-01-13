package controllers

import domain.GlossaryPageResponse
import models.Glossary
import play.api.data.validation.ValidationError
import play.api.i18n.{Lang, Messages}
import play.api.libs.json._
import play.api.mvc._
import services.GlossaryService

object GlossariesRestController extends SecuredController {

  implicit val gf = Json.format[Glossary]
  implicit val gpf = Json.format[GlossaryPageResponse]

  def getGlossaries(startRow: Int, pageSize: Int) = authenticated {
    Action {
      val glossariesPage = GlossaryService.getCurrentPage(startRow, pageSize)

      Ok(Json.toJson(glossariesPage))
    }
  }

  def getGlossary(id: Long) = authenticated {
    Action {
      GlossaryService.getGlossaryById(id) match {
        case Some(glossary) =>
          Ok(Json.toJson(glossary))
        case _ =>
          BadRequest(Messages("sample.error.glossary.not.found", id))
      }
    }
  }

  def removeGlossary(glossaryId: Long) =  asAdmin {
    Action {
      GlossaryService.removeGlossaryById(glossaryId)

      Ok
    }
  }

  def updateGlossary() = asAdmin {
    saveUpdate {GlossaryService.updateGlossary}
  }

  def saveGlossary = asAdmin {
    saveUpdate {GlossaryService.addGlossary}
  }

  private def saveUpdate(action: Glossary => Unit) = Action(parse.json) {
    implicit request =>
      request.body.validate[Glossary] match {
        case JsSuccess(glossary, _) =>
          action(glossary)
          Ok
        case JsError(errors) =>
          val validationResponse = handleErrors(errors)

          BadRequest(Json.toJson(validationResponse))
      }
  }

  //use dot method call just as Martin Odersky recommends
  //https://twitter.com/odersky/status/49882758968905728
  private def handleErrors(errors: Seq[(JsPath, Seq[ValidationError])])(implicit lang: Lang): Map[String, Array[String]] = {
    (errors map {
      case (jsPath, validationErrors) =>
        val key = jsPath.toString()
        val value = validationErrors.map(valError => Messages(valError.message)).toArray

        //return tuple, which naturally transforms into map
        (key, value)
    }).toMap
  }

}
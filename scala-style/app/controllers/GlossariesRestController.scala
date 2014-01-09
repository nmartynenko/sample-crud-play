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

  def getGlossaries(startRow: Int, pageSize: Int) = Action {
    val glossariesPage = GlossaryService.getCurrentPage(startRow, pageSize)

    Ok(Json.toJson(glossariesPage))
  }

  def getGlossary(id: Long) = Action {
    val glossary = GlossaryService.getGlossaryById(id)

    Ok(Json.toJson(glossary))
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


  def updateGlossary() = saveUpdate {GlossaryService.updateGlossary}

  def saveGlossary = saveUpdate {GlossaryService.addGlossary}

  def removeGlossary(glossaryId: Long) =  Action {
    GlossaryService.removeGlossaryById(glossaryId)

    Ok
  }

}
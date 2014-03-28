package controllers

import com.aimprosoft.play.glossaries.domain.GlossaryPageResponse
import com.aimprosoft.play.glossaries.models.Glossary
import com.aimprosoft.play.glossaries.service.GlossaryService
import play.api.data.validation.ValidationError
import play.api.http.ContentTypes
import play.api.i18n.{Lang, Messages}
import play.api.libs.json._
import play.api.mvc._

object GlossariesRestController extends SecuredController {

  implicit val gf = Json.format[Glossary]
  implicit val gpf = Json.format[GlossaryPageResponse]

  def getGlossaries(startRow: Int, pageSize: Int) = authenticated {
    Action {
      //get generic page
      val page = GlossaryService.getCurrentPage(startRow, pageSize)
      //convert into concrete response page
      val glossariesPage = GlossaryPageResponse(page.content, page.startRow, page.pageSize, page.totalElements)

      Ok(Json.toJson(glossariesPage)).as(ContentTypes.JSON)
    }
  }

  def getGlossary(id: Long) = authenticated {
    Action {
      GlossaryService.getById(id) match {
        case Some(glossary) =>
          Ok(Json.toJson(glossary)).as(ContentTypes.JSON)
        case _ =>
          BadRequest(Messages("sample.error.glossary.not.found", id))
      }
    }
  }

  def removeGlossary(glossaryId: Long) =  asAdmin {
    Action {
      GlossaryService.removeById(glossaryId)

      Ok
    }
  }

  def updateGlossary() = asAdmin {
    saveUpdate {GlossaryService.update}
  }

  def saveGlossary = asAdmin {
    saveUpdate {GlossaryService.add}
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
  private def handleErrors(errors: Seq[(JsPath, Seq[ValidationError])])(implicit lang: Lang): Map[String, Seq[String]] = {
    (errors map {
      case (jsPath, validationErrors) =>
        val key = jsPath.toString()
        val value = validationErrors map {
          valError => Messages(valError.message)
        }

        //return tuple, which naturally transforms into map
        (key, value)
    }).toMap
  }

}
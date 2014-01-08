package controllers

import models.Glossary
import play.api.libs.json.Json
import play.api.mvc._
import services.GlossaryService
import domain.GlossaryPageResponse

class GlossariesRestController extends Controller{

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

  def saveGlossary() = Action(parse.json) {request =>
    val glossary = request.body.as[Glossary]

    GlossaryService.addGlossary(glossary)

    Ok
  }

  def updateGlossary() = Action(parse.json) {request =>
    val glossary = request.body.as[Glossary]

    GlossaryService.updateGlossary(glossary)

    Ok
  }

  def removeGlossary(glossaryId: Long) =  Action {
    GlossaryService.removeGlossaryById(glossaryId)

    Ok
  }

}
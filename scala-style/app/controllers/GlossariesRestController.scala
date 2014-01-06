package controllers

import play.api.mvc._
import services.GlossaryService
import play.api.libs.json.Json
import models.Glossary

class GlossariesRestController extends Controller {

  implicit val glossaryFormat = Json.format[Glossary]

  def getGlossaries(startRow: Int, pageSize: Int) = TODO

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

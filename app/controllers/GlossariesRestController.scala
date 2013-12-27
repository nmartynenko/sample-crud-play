package controllers

import com.fasterxml.jackson.databind.ObjectMapper
import models.impl.Glossary
import org.springframework.beans.factory.annotation.Autowired
import play.api.mvc._
import services.GlossaryService
import vo.GlossaryList

@MVCController
class GlossariesRestController extends Controller{

  @Autowired
  private val objectMapper: ObjectMapper = null

  @Autowired
  private val glossaryService: GlossaryService = null
  def getGlossaries(startRow: Int, pageSize: Int) = Action {
    val glossaries = GlossaryList(glossaryService.getCurrentPage(startRow, pageSize))

    Ok(objectMapper.writeValueAsString(glossaries))
  }

  def getGlossary(id: Long) = Action {
    val glossary = glossaryService.getGlossaryById(id)

    Ok(objectMapper.writeValueAsString(glossary))
  }

  def saveGlossary(glossary: Glossary) = TODO

  def updateGlossary(glossary: Glossary) = TODO

  def removeGlossary(glossaryId: Long) = TODO

}

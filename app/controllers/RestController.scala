package controllers

import play.api.mvc._
import model.impl.Glossary
import org.springframework.beans.factory.annotation.Autowired
import service.GlossaryService

@org.springframework.stereotype.Controller
class RestController extends Controller{

  @Autowired
  private val glossaryService: GlossaryService = null

  def getGlossaries(startRow: Int, pageSize: Int) = TODO

  def getGlossary(id: Long) = TODO

  def saveGlossary(lossary: Glossary) = TODO

  def updateGlossary(glossary: Glossary) = TODO

  def removeGlossary(glossaryId: Long) = TODO
}

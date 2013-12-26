package controllers

import play.api.mvc._
import models.impl.Glossary
import org.springframework.beans.factory.annotation.Autowired
import services.GlossaryService

//Java2Scala conversions and vice versa
import scala.collection.JavaConversions._

@MVCController
class GlossariesController extends Controller{

  @Autowired
  private val glossaryService: GlossaryService = null

  private val HOME = Redirect("/index.html")

  def index() = Action {HOME}

  def getGlossaries(startRow: Int, pageSize: Int) = Action {
    Ok(views.html.list(glossaryService.getCurrentPage(startRow, pageSize)))
  }

  def getGlossary(id: Long) = TODO

  def saveGlossary(lossary: Glossary) = TODO

  def updateGlossary(glossary: Glossary) = TODO

  def removeGlossary(glossaryId: Long) = TODO

}

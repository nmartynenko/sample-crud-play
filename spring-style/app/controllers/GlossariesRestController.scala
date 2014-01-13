package controllers

import com.fasterxml.jackson.databind.ObjectReader
import models.impl.Glossary
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import play.api.http.ContentTypes
import play.api.mvc._
import services.GlossaryService
import vo.GlossaryList

@MVCController
@PreAuthorize("isAuthenticated()")
class GlossariesRestController extends BaseController with InitializingBean {

  @Autowired
  private val glossaryService: GlossaryService = null

  //we are using ObjectMapper/ObjectReader, because
  //Json.format/reads/writes macros support case classes only,
  //but Hibernate doesn't support them,
  //meanwhile generating of compiled JSON read/writes
  //doesn't have big point from our perspective
  //P.S. Yeap, we are lazy
  private var glossaryReader: ObjectReader = _

  def afterPropertiesSet(): Unit = {
    glossaryReader = objectMapper.reader(classOf[Glossary])
  }

  def getGlossaries(startRow: Int, pageSize: Int) = Action {
    val glossaries = GlossaryList(glossaryService.getCurrentPage(startRow, pageSize))

    val json = objectMapper.writeValueAsString(glossaries)

    Ok(json).as(ContentTypes.JSON)
  }

  def getGlossary(id: Long) = Action {
    val glossary = glossaryService.getGlossaryById(id)

    val json = objectMapper.writeValueAsString(glossary)

    Ok(json).as(ContentTypes.JSON)
  }

  //treat input value as tolerant text
  def saveGlossary() = Action(parse.tolerantText) {request =>
    val glossary = glossaryReader.readValue[Glossary](request.body)

    glossaryService.addGlossary(glossary)

    Ok
  }

  //treat input value as tolerant text
  def updateGlossary() = Action(parse.tolerantText) {request =>
    val glossary = glossaryReader.readValue[Glossary](request.body)

    glossaryService.updateGlossary(glossary)

    Ok
  }

  def removeGlossary(glossaryId: Long) = Action {
    glossaryService.removeGlossaryById(glossaryId)

    Ok
  }

}

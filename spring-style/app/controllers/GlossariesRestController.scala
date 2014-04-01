package controllers

import com.aimprosoft.play.glossaries.models.impl.Glossary
import com.aimprosoft.play.glossaries.service.GlossaryService
import com.aimprosoft.play.glossaries.vo.GlossaryList
import com.aimprosoft.scala.contrib.oval.ScalaOvalValidator
import com.fasterxml.jackson.databind.ObjectReader
import net.sf.oval.ConstraintViolation
import net.sf.oval.context.FieldContext
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import play.api.http.ContentTypes
import play.api.i18n.Messages
import play.api.libs.json.Json
import play.api.mvc._

@MVCController
@PreAuthorize("isAuthenticated()")
class GlossariesRestController extends BaseController with InitializingBean {

  @Autowired
  private val validator: ScalaOvalValidator = null

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
    val glossary = glossaryService.getById(id)

    val json = objectMapper.writeValueAsString(glossary)

    Ok(json).as(ContentTypes.JSON)
  }

  @PreAuthorize("hasAnyRole('ADMIN')")
  def removeGlossary(glossaryId: Long) = Action {
    glossaryService.removeById(glossaryId)

    Ok
  }

  //treat input value as tolerant text
  @PreAuthorize("hasAnyRole('ADMIN')")
  def saveGlossary() = saveUpdate {glossaryService.add}

  @PreAuthorize("hasAnyRole('ADMIN')")
  def updateGlossary() = saveUpdate {glossaryService.update}

  //treat input value as tolerant text
  private def saveUpdate(action: Glossary => Unit) = Action(parse.tolerantText) {
    implicit request =>
      val glossary = glossaryReader.readValue[Glossary](request.body)

      validator.validate(glossary) match {
        case Nil =>
          action(glossary)
          Ok
        case constraints =>
          val validationResponse = handleErrors(constraints)

          BadRequest(Json.toJson(validationResponse))
      }
  }

  private def handleErrors(constraints: List[ConstraintViolation]): Map[String, String] = {
    (constraints map { constraint =>
      val key = constraint.getContext match {
        case fc: FieldContext => fc.getField.getName
        case _ => constraint.getErrorCode
      }
      val value =  Messages(constraint.getMessage)

      (key, value)
    }).toMap
  }

}

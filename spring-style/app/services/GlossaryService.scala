package services

import exceptions.GlossaryException
import java.lang.Long
import models.impl.Glossary
import org.springframework.data.domain.Page
import scala.throws

trait GlossaryService {

  @throws[GlossaryException]
  def getCurrentPage(startRow: Int, pageSize: Int): Page[Glossary]

  @throws[GlossaryException]
  def getGlossaryById(glossaryId: Long): Glossary

  @throws[GlossaryException]
  def addGlossary(glossary: Glossary): Unit

  @throws[GlossaryException]
  def updateGlossary(glossary: Glossary): Unit

  @throws[GlossaryException]
  def removeGlossary(glossary: Glossary): Unit

  @throws[GlossaryException]
  def removeGlossaryById(glossaryId: Long): Unit

}
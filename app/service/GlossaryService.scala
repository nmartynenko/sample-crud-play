package service

import java.lang.Long
import org.springframework.data.domain.Page
import org.springframework.security.access.prepost.PreAuthorize
import scala.throws
import exception.GlossaryException
import model.impl.Glossary

trait GlossaryService {

  @throws[GlossaryException]
  def getCurrentPage(startRow: Int, pageSize: Int): Page[Glossary]

  @throws[GlossaryException]
  def getGlossaryById(glossaryId: Long): Glossary

  @throws[GlossaryException]
  @PreAuthorize("hasRole('ADMIN')")
  def addGlossary(glossary: Glossary): Unit

  @throws[GlossaryException]
  @PreAuthorize("hasRole('ADMIN')")
  def updateGlossary(glossary: Glossary): Unit

  @throws[GlossaryException]
  @PreAuthorize("hasRole('ADMIN')")
  def removeGlossary(glossary: Glossary): Unit

  @throws[GlossaryException]
  @PreAuthorize("hasRole('ADMIN')")
  def removeGlossaryById(glossaryId: Long): Unit

}
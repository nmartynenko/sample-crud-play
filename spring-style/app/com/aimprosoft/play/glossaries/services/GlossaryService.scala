package com.aimprosoft.play.glossaries.services

import com.aimprosoft.play.glossaries.exceptions.GlossaryException
import com.aimprosoft.play.glossaries.models.impl.Glossary
import org.springframework.data.domain.Page

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
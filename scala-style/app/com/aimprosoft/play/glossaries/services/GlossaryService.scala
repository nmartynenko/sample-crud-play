package com.aimprosoft.play.glossaries.services

import com.aimprosoft.play.glossaries.domain.GlossaryPageResponse
import com.aimprosoft.play.glossaries.models.Glossary
import com.aimprosoft.play.glossaries.services.impl.GlossaryServiceImpl

trait GlossaryService {

  def getCurrentPage(startRow: Int, pageSize: Int): GlossaryPageResponse

  def getGlossaryById(glossaryId: Long): Option[Glossary]

  def addGlossary(glossary: Glossary): Unit

  def updateGlossary(glossary: Glossary): Unit

  def removeGlossary(glossary: Glossary): Unit

  def removeGlossaryById(glossaryId: Long): Unit

}

object GlossaryService extends GlossaryServiceImpl
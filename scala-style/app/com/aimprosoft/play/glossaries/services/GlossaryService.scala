package com.aimprosoft.play.glossaries.services

import com.aimprosoft.play.glossaries.domain.GlossaryPageResponse
import com.aimprosoft.play.glossaries.models.Glossary
import com.aimprosoft.play.glossaries.services.impl.GlossaryServiceImpl

trait GlossaryService {

  def getCurrentPage(startRow: Int, pageSize: Int): GlossaryPageResponse

  def getById(glossaryId: Long): Option[Glossary]

  def add(glossary: Glossary): Unit

  def update(glossary: Glossary): Unit

  def remove(glossary: Glossary): Unit

  def removeById(glossaryId: Long): Unit

}

object GlossaryService extends GlossaryServiceImpl
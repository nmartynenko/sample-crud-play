package com.aimprosoft.play.glossaries.services.impl

import com.aimprosoft.play.glossaries.domain.GlossaryPageResponse
import com.aimprosoft.play.glossaries.models.Glossary
import com.aimprosoft.play.glossaries.persistence.GlossaryPersistence
import com.aimprosoft.play.glossaries.services.GlossaryService
import play.api.db.slick.Session

class GlossaryServiceImpl extends GlossaryService with SlickTransactional {

  def getCurrentPage(startRow: Int, pageSize: Int): GlossaryPageResponse = readOnly {
    implicit session: Session => {
      val content = GlossaryPersistence.list(startRow, pageSize)
      
      val te = if (pageSize < 0) content.size else GlossaryPersistence.count
      val sr = if (startRow < 0) 0 else startRow
      val ps = if (pageSize > 0) pageSize else te

      GlossaryPageResponse(content, sr, ps, te)
    }
  }


  def getGlossaryById(glossaryId: Long): Option[Glossary] = readOnly {
    implicit session: Session =>
      GlossaryPersistence.get(glossaryId)
  }

  def addGlossary(glossary: Glossary): Unit = transactional {
    implicit session: Session =>
      GlossaryPersistence.insert(glossary)
  }

  def updateGlossary(glossary: Glossary): Unit = transactional {
    implicit session: Session =>
      GlossaryPersistence.update(glossary)
  }

  def removeGlossary(glossary: Glossary): Unit = removeGlossaryById(glossary.id.get)

  def removeGlossaryById(glossaryId: Long): Unit = transactional {
    implicit session: Session =>
      GlossaryPersistence.delete(glossaryId)
  }
}

package com.aimprosoft.play.glossaries.services.impl

import com.aimprosoft.play.glossaries.domain.GlossaryPageResponse
import com.aimprosoft.play.glossaries.models.Glossary
import com.aimprosoft.play.glossaries.persistence.GlossaryPersistence
import com.aimprosoft.play.glossaries.services.GlossaryService
import play.api.db.slick.Session

class GlossaryServiceImpl extends GlossaryService with SlickTransactional {

  def getCurrentPage(startRow: Int, pageSize: Int): GlossaryPageResponse = readOnly {
    implicit session: Session => {
      //list of entities
      val content = GlossaryPersistence.list(startRow, pageSize)

      //number of all elements in DB
      val te = {
        //quick check for not working with DB
        if (startRow <= 0 && pageSize < 0)
          content.size
        //check in DB otherwise
        else
          GlossaryPersistence.count
      }

      //adjust start row
      val sr = if (startRow < 0) 0 else startRow
      //adjust page size
      val ps = if (pageSize > 0) pageSize else te

      GlossaryPageResponse(content, sr, ps, te)
    }
  }


  def getById(glossaryId: Long): Option[Glossary] = readOnly {
    implicit session: Session =>
      GlossaryPersistence.get(glossaryId)
  }

  def add(glossary: Glossary): Unit = transactional {
    implicit session: Session =>
      GlossaryPersistence.insert(glossary)
  }

  def update(glossary: Glossary): Unit = transactional {
    implicit session: Session =>
      GlossaryPersistence.update(glossary)
  }

  def remove(glossary: Glossary): Unit = glossary.id foreach { id =>
    removeById(id)
  }

  def removeById(glossaryId: Long): Unit = transactional {
    implicit session: Session =>
      GlossaryPersistence.delete(glossaryId)
  }
}

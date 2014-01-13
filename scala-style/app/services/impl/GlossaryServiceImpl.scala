package services.impl

import domain.GlossaryPageResponse
import exceptions.NoGlossaryFoundException
import java.lang.Long
import models.Glossary
import persistence.GlossaryPersistence
import play.api.db.slick.Session
import services.GlossaryService

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
      GlossaryPersistence.update(glossary.id.get, glossary)
  }

  def removeGlossary(glossary: Glossary): Unit = removeGlossaryById(glossary.id.get)

  def removeGlossaryById(glossaryId: Long): Unit = transactional {
    implicit session: Session =>
      GlossaryPersistence.delete(glossaryId)
  }
}

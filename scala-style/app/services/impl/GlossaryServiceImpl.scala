package services.impl

import java.lang.Long
import models.Glossary
import persistence.GlossaryPersistence
import play.api.Play.current
import play.api.db.slick.DB
import play.api.db.slick.Session
import services.GlossaryService

class GlossaryServiceImpl extends GlossaryService {

  def getCurrentPage(startRow: Int, pageSize: Int): Seq[Glossary] = DB.withSession {
    implicit session: Session =>
      GlossaryPersistence.list(startRow, pageSize)
  }

  def getGlossaryById(glossaryId: Long): Glossary = DB.withSession {
    implicit session: Session =>
      GlossaryPersistence.get(glossaryId)
  }

  def addGlossary(glossary: Glossary): Unit = DB.withTransaction {
    implicit session: Session =>
      GlossaryPersistence.insert(glossary)
  }

  def updateGlossary(glossary: Glossary): Unit = DB.withTransaction {
    implicit session: Session =>
      GlossaryPersistence.update(glossary.id.get, glossary)
  }

  def removeGlossary(glossary: Glossary): Unit = removeGlossaryById(glossary.id.get)

  def removeGlossaryById(glossaryId: Long): Unit = DB.withTransaction {
    implicit session: Session =>
      GlossaryPersistence.delete(glossaryId)
  }
}

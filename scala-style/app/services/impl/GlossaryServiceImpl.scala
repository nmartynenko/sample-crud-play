package services.impl

import java.lang.Long
import services.GlossaryService
import models.Glossary
import persistence.GlossaryPersistence

//todo move transaction layer here
class GlossaryServiceImpl extends GlossaryService {

  def getCurrentPage(startRow: Int, pageSize: Int): List[Glossary] = ???

  def getGlossaryById(glossaryId: Long): Glossary = GlossaryPersistence.get(glossaryId)

  def addGlossary(glossary: Glossary): Unit = ???

  def updateGlossary(glossary: Glossary): Unit = ???

  def removeGlossary(glossary: Glossary): Unit = ???

  def removeGlossaryById(glossaryId: Long): Unit = ???
}

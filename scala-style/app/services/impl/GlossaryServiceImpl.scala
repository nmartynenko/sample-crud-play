package services.impl

import java.lang.Long
import services.GlossaryService
import models.Glossary

object GlossaryServiceImpl extends GlossaryService {
  def getCurrentPage(startRow: Int, pageSize: Int): List[Glossary] = ???

  def getGlossaryById(glossaryId: Long): Glossary = ???

  def addGlossary(glossary: Glossary): Unit = ???

  def updateGlossary(glossary: Glossary): Unit = ???

  def removeGlossary(glossary: Glossary): Unit = ???

  def removeGlossaryById(glossaryId: Long): Unit = ???
}

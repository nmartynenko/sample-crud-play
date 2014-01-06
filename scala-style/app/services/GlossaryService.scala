package services

import java.lang.Long
import services.impl.GlossaryServiceImpl
import models.Glossary

trait GlossaryService {

  def getCurrentPage(startRow: Int, pageSize: Int): List[Glossary]

  def getGlossaryById(glossaryId: Long): Glossary

  def addGlossary(glossary: Glossary): Unit

  def updateGlossary(glossary: Glossary): Unit

  def removeGlossary(glossary: Glossary): Unit

  def removeGlossaryById(glossaryId: Long): Unit

}

object GlossaryService extends GlossaryService {
  //assign default implementation
  private def IMPL = GlossaryServiceImpl
  
  def getCurrentPage(startRow: Int, pageSize: Int): List[Glossary] = IMPL.getCurrentPage(startRow, pageSize)

  def getGlossaryById(glossaryId: Long): Glossary = IMPL.getGlossaryById(glossaryId)

  def addGlossary(glossary: Glossary): Unit = IMPL.addGlossary(glossary)

  def updateGlossary(glossary: Glossary): Unit = IMPL.updateGlossary(glossary)

  def removeGlossary(glossary: Glossary): Unit = IMPL.removeGlossary(glossary)

  def removeGlossaryById(glossaryId: Long): Unit = IMPL.removeGlossaryById(glossaryId)
}
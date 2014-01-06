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

object GlossaryService extends GlossaryServiceImpl with GlossaryService
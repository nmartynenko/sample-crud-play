package services.impl

import exceptions.{NoGlossaryFoundException, GlossaryException}
import java.lang.Long
import models.impl.Glossary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.{Page, PageRequest,Pageable}
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import persistence.GlossaryPersistence
import services.GlossaryService

@Service
class GlossaryServiceImpl extends GlossaryService {

  @Autowired
  private val glossaryPersistence: GlossaryPersistence = null

  @throws[GlossaryException]
  def getCurrentPage(startRow: Int, pageSize: Int): Page[Glossary] = {
    var pageable: Pageable = null

    if (startRow >= 0 && pageSize > 0) {
      pageable = new PageRequest(startRow / pageSize, pageSize)
    }

    try {
      glossaryPersistence.findAll(pageable)
    } catch {
      case e: RuntimeException => throw new GlossaryException(null, e)
    }
  }

  @throws[GlossaryException]
  def getGlossaryById(glossaryId: Long): Glossary = {
    try {
      val glossary = glossaryPersistence.findOne(glossaryId)

      if (glossary == null) {
        throw new NoGlossaryFoundException(null, glossaryId)
      }

      glossary
    } catch {
      case e: RuntimeException => throw new GlossaryException(null, e)
    }
  }

  @throws[GlossaryException]
  @PreAuthorize("hasRole('ADMIN')")
  def addGlossary(glossary: Glossary): Unit = {
    try {
      glossaryPersistence.save(glossary)
    } catch {
      case e: RuntimeException => throw new GlossaryException(null, e)
    }
  }

  @throws[GlossaryException]
  @PreAuthorize("hasRole('ADMIN')")
  def updateGlossary(glossary: Glossary): Unit = {
    try {
      glossaryPersistence.save(glossary)
    } catch {
      case e: EmptyResultDataAccessException => throw new NoGlossaryFoundException(e, glossary.id)
      case e: RuntimeException => throw new GlossaryException(null, e)
    }
  }

  @throws[GlossaryException]
  @PreAuthorize("hasRole('ADMIN')")
  def removeGlossary(glossary: Glossary): Unit = {
    try {
      glossaryPersistence.delete(glossary)
    } catch {
      case e: EmptyResultDataAccessException => throw new NoGlossaryFoundException(e, glossary.id)
      case e: RuntimeException => throw new GlossaryException(null, e)
    }
  }

  @throws[GlossaryException]
  @PreAuthorize("hasRole('ADMIN')")
  def removeGlossaryById(glossaryId: Long): Unit = {
    try {
      glossaryPersistence.delete(glossaryId)
    } catch {
      case e: EmptyResultDataAccessException => throw new NoGlossaryFoundException(e, glossaryId)
      case e: RuntimeException => throw new GlossaryException(null, e)
    }
  }
}

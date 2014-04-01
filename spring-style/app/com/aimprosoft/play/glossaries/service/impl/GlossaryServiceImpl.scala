package com.aimprosoft.play.glossaries.service.impl

import com.aimprosoft.play.glossaries.models.impl.Glossary
import com.aimprosoft.play.glossaries.persistence.GlossaryPersistence
import com.aimprosoft.play.glossaries.service.GlossaryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GlossaryServiceImpl extends BaseCrudServiceImpl[Glossary, GlossaryPersistence] with GlossaryService {

  @Autowired
  protected val persistence: GlossaryPersistence = null
}

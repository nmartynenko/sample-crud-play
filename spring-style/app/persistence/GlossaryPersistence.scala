package persistence

import java.lang.Long
import models.impl.Glossary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

@Transactional
trait GlossaryPersistence extends JpaRepository[Glossary, Long]
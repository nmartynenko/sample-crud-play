package com.aimprosoft.play.glossaries.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import com.aimprosoft.play.glossaries.models.impl.Glossary

@Transactional
trait GlossaryPersistence extends JpaRepository[Glossary, java.lang.Long]
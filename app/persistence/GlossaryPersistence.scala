package persistence

import java.lang.Long
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import model.impl.Glossary

@Transactional
trait GlossaryPersistence extends JpaRepository[Glossary, Long]
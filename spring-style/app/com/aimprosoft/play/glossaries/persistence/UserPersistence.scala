package com.aimprosoft.play.glossaries.persistence

import com.aimprosoft.play.glossaries.models.impl.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

@Transactional
trait UserPersistence extends JpaRepository[User, java.lang.Long]{

    def findByEmail(email: String): User

}

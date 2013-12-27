package persistence

import java.lang.Long
import models.impl.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

@Transactional
trait UserPersistence extends JpaRepository[User, Long]{

    def findByEmail(email: String): User

}

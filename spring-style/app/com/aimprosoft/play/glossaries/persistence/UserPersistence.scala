package com.aimprosoft.play.glossaries.persistence

import com.aimprosoft.play.glossaries.models.impl.User
import org.springframework.data.jpa.repository.{Query, JpaRepository}
import org.springframework.transaction.annotation.Transactional
import com.aimprosoft.play.glossaries.models.UserRole
import org.springframework.data.repository.query.Param

@Transactional
trait UserPersistence extends JpaRepository[User, java.lang.Long] {

  def findByEmail(email: String): User

  @Query("select count(u) from User u where u.role = :role")
  def countByRole(@Param("role") role: UserRole): Long

}

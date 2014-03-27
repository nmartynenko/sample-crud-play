package com.aimprosoft.play.glossaries.services.impl

import com.aimprosoft.play.glossaries.models.User
import com.aimprosoft.play.glossaries.persistence.UserPersistence
import com.aimprosoft.play.glossaries.services.UserService
import org.mindrot.jbcrypt.BCrypt
import play.api.db.slick.Session

class UserServiceImpl extends UserService with SlickTransactional {

  def add(user: User): Unit = readOnly {
    implicit session: Session =>
      //hash plain text password
      val hashedPassword = BCrypt.hashpw(user.password, BCrypt.gensalt())

      UserPersistence.insert(user.copy(password = hashedPassword))
  }

  def getByEmail(email: String): Option[User] = transactional {
    implicit session: Session =>
      UserPersistence.findByEmail(email)
  }
}

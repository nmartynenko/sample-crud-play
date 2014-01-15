package services.impl

import models.User
import persistence.UserPersistence
import play.api.db.slick.Session
import services.UserService
import org.mindrot.jbcrypt.BCrypt

class UserServiceImpl extends UserService with SlickTransactional {

  def addUser(user: User): Unit = readOnly {
    implicit session: Session =>
      //hash plain text password
      val hashedPassword = BCrypt.hashpw(user.password, BCrypt.gensalt())

      UserPersistence.insert(user copy (password = hashedPassword))
  }

  def getUserByEmail(email: String): User = transactional {
    implicit session: Session =>
      UserPersistence.findByEmail(email)
  }
}

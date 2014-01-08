package services.impl

import models.User
import persistence.UserPersistence
import play.api.db.slick.Session
import services.UserService

class UserServiceImpl extends UserService with SlickTransactional {

  def addUser(user: User): Unit = readOnly {
    implicit session: Session =>
      UserPersistence.insert(user)
  }

  def getUserByEmail(email: String): User = transactional {
    implicit session: Session =>
      UserPersistence.findByEmail(email)
  }
}

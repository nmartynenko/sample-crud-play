package services.impl

import models.User
import persistence.UserPersistence
import play.api.Play.current
import play.api.db.slick.DB
import play.api.db.slick.Session
import services.UserService

class UserServiceImpl extends UserService {

  def addUser(user: User): Unit = DB.withTransaction {
    implicit session: Session =>
      UserPersistence.insert(user)
  }

  def getUserByEmail(email: String): User = DB.withSession {
    implicit session: Session =>
      UserPersistence.findByEmail(email)
  }
}

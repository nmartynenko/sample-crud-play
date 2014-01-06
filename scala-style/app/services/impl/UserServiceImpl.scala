package services.impl

import services.UserService
import models.User

object UserServiceImpl extends UserService {

  def addUser(user: User): Unit = ???

  def getUserByEmail(username: String): User = ???
}

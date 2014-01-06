package services.impl

import services.UserService
import models.User

class UserServiceImpl extends UserService {

  def addUser(user: User): Unit = ???

  def getUserByEmail(username: String): User = ???
}

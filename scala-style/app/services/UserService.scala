package services

import services.impl.UserServiceImpl
import models.User

trait UserService {

  def addUser(user: User)

  def getUserByEmail(username: String): User
}

object UserService extends UserServiceImpl with UserService
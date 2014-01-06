package services

import services.impl.UserServiceImpl
import models.User

trait UserService {

  def addUser(user: User)

  def getUserByEmail(username: String): User
}

object UserService extends UserService {
  //assign default implementation
  private def IMPL = UserServiceImpl

  def addUser(user: User) = IMPL.addUser(user)

  def getUserByEmail(username: String): User = IMPL.getUserByEmail(username)
}
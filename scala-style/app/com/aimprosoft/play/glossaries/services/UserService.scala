package com.aimprosoft.play.glossaries.services

import com.aimprosoft.play.glossaries.models.User
import com.aimprosoft.play.glossaries.services.impl.UserServiceImpl

trait UserService {

  def addUser(user: User)

  def getUserByEmail(username: String): User
}

object UserService extends UserServiceImpl
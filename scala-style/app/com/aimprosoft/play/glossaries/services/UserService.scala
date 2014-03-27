package com.aimprosoft.play.glossaries.services

import com.aimprosoft.play.glossaries.models.User
import com.aimprosoft.play.glossaries.services.impl.UserServiceImpl

trait UserService {

  def add(user: User)

  def getByEmail(username: String): Option[User]
}

object UserService extends UserServiceImpl
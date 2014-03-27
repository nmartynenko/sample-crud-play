package com.aimprosoft.play.glossaries.listeners

import com.aimprosoft.play.glossaries.models.User
import com.aimprosoft.play.glossaries.services.UserService
import play.api.Logger

object CreateUserListener extends Listener {

  def init() {
    Logger.info("Start adding sample user")

    val user = new User(
      email = "user@example.com",
      password = "user",
      name = "Sample User",
      role = "USER"
    )

    //add user if it doesn't exists
    UserService.getByEmail(user.email).getOrElse {
      UserService.add(user)
    }

    Logger.info("Sample user has been added successfully")
  }
}

package com.aimprosoft.play.glossaries.listeners

import com.aimprosoft.play.glossaries.models.User
import com.aimprosoft.play.glossaries.service.UserService
import play.api.Logger

object CreateAdminListener extends Listener{

  def init() {
    Logger.info("Start adding sample admin")

    val user = new User(
      email = "admin@example.com",
      password = "admin",
      name = "Sample Admin",
      role = "ADMIN"
    )

    //add user if it doesn't exists
    UserService.getByEmail(user.email).getOrElse {
      UserService.add(user)
    }

    Logger.info("Sample admin has been added successfully")
  }
}

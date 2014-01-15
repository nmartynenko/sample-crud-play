package com.aimprosoft.play.glossaries.listeners

import com.aimprosoft.play.glossaries.models.User
import com.aimprosoft.play.glossaries.services.UserService
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

    UserService.addUser(user)

    Logger.info("Sample admin has been added successfully")
  }
}

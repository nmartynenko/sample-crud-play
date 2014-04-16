package com.aimprosoft.play.glossaries.listeners

import com.aimprosoft.play.glossaries.models.User
import com.aimprosoft.play.glossaries.security.UserRole
import com.aimprosoft.play.glossaries.service.UserService
import play.api.Logger

object CreateUserListener extends Listener {

  def init() {
    //if regular user is not present yet
    if (UserService.countByRole(UserRole) == 0) {
      Logger.info("Start adding sample user")

      val user = new User(
        email = "user@example.com",
        password = "user",
        name = "Sample User",
        role = "USER"
      )

      UserService.add(user)

      Logger.info("Sample user has been added successfully")
    }
  }
}

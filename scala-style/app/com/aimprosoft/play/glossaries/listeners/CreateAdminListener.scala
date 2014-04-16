package com.aimprosoft.play.glossaries.listeners

import com.aimprosoft.play.glossaries.models.User
import com.aimprosoft.play.glossaries.security.AdminRole
import com.aimprosoft.play.glossaries.service.UserService
import play.api.Logger

object CreateAdminListener extends Listener{

  def init() {
    //if admin is not present yet
    if (UserService.countByRole(AdminRole) == 0){
      Logger.info("Start adding sample admin")

      val user = new User(
        email = "admin@example.com",
        password = "admin",
        name = "Sample Admin",
        role = "ADMIN"
      )

      UserService.add(user)

      Logger.info("Sample admin has been added successfully")
    }
  }
}

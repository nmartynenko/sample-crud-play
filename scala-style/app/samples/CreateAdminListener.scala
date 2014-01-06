package samples

import services.UserService
import play.api.Logger
import models.User

class CreateAdminListener {

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

package listeners

import play.api.Logger
import services.UserService
import models.User

object CreateUserListener extends Listener {

  def init() {
    Logger.info("Start adding sample user")

    val user = new User(
      email = "user@example.com",
      password = "user",
      name = "Sample User",
      role = "USER"
    )

    UserService.addUser(user)

    Logger.info("Sample user has been added successfully")
  }
}

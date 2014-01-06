package samples

import javax.annotation.PostConstruct
import models.impl.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import play.api.Logger
import services.UserService

@Service
class CreateUserListener {

  @Autowired
  private val userService: UserService = null

  @PostConstruct
  @throws[Exception]
  def init() {
    Logger.info("Start adding sample user")

    val user = new User()
    user.email = "user@example.com"
    user.password = "user"
    user.name = "Sample User"
//    user.role = UserRole.USER
    user.role = "USER"

    userService.addUser(user)

    Logger.info("Sample user has been added successfully")
  }
}

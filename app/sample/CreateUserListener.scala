package sample

import javax.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import service.UserService
import model.impl.User

@Service
class CreateUserListener {

  private val _logger = LoggerFactory.getLogger(getClass)

  @Autowired
  private val userService: UserService = null

  @PostConstruct
  @throws[Exception]
  def init() {
    _logger.info("Start adding sample user")

    val user = new User()
    user.email = "user@example.com"
    user.password = "user"
    user.name = "Sample User"
//    user.role = UserRole.USER
    user.role = "USER"

    userService.addUser(user)

    _logger.info("Sample user has been added successfully")
  }
}

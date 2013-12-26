package samples

import javax.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import services.UserService
import models.impl.User
;

@Service
class CreateAdminListener {

  private val _logger = LoggerFactory.getLogger(getClass)

  @Autowired
  private val userService: UserService = null

  @PostConstruct
  @throws[Exception]
  def init() {
    _logger.info("Start adding sample admin")

    val user = new User()
    user.email = "admin@example.com"
    user.password = "admin"
    user.name = "Sample Admin"
//    user.role = UserRole.ADMIN
    user.role = "ADMIN"

    userService.addUser(user)

    _logger.info("Sample admin has been added successfully")
  }
}

package com.aimprosoft.play.glossaries.samples

import com.aimprosoft.play.glossaries.models.UserRole
import com.aimprosoft.play.glossaries.models.impl.User
import com.aimprosoft.play.glossaries.services.UserService
import javax.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import play.api.Logger

@Service
class CreateAdminListener {

  @Autowired
  private val userService: UserService = null

  @PostConstruct
  @throws[Exception]
  def init() {
    Logger.info("Start adding sample admin")

    val user = new User()
    user.email = "admin@example.com"
    user.password = "admin"
    user.name = "Sample Admin"
    user.role = UserRole.ADMIN

    userService.addUser(user)

    Logger.info("Sample admin has been added successfully")
  }
}

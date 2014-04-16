package com.aimprosoft.play.glossaries.samples

import com.aimprosoft.play.glossaries.models.UserRole
import com.aimprosoft.play.glossaries.models.impl.User
import com.aimprosoft.play.glossaries.service.UserService
import javax.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import play.api.Logger

@Service
class CreateUserListener {

  @Autowired
  private val userService: UserService = null

  @PostConstruct
  @throws[Exception]
  def init() {
    //check whether data is present in DB
    if (userService.countByRole(UserRole.USER) == 0) {
      Logger.info("Start adding sample user")

      val user = new User()
      user.email = "user@example.com"
      user.password = "user"
      user.name = "Sample User"
      user.role = UserRole.USER

      userService.add(user)

      Logger.info("Sample user has been added successfully")
    }
  }
}

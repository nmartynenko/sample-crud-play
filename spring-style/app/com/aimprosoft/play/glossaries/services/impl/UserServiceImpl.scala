package com.aimprosoft.play.glossaries.services.impl

import com.aimprosoft.play.glossaries.models.impl.User
import com.aimprosoft.play.glossaries.persistence.UserPersistence
import com.aimprosoft.play.glossaries.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.{NoOpPasswordEncoder, PasswordEncoder}
import org.springframework.stereotype.Service

@Service
class UserServiceImpl extends UserService {

  @Autowired
  private val userPersistence: UserPersistence = null

  @Autowired(required = false)
  private val passwordEncoder: PasswordEncoder = NoOpPasswordEncoder.getInstance

  def addUser(user: User): Unit = {
    user.password = passwordEncoder.encode(user.password)

    userPersistence.save(user)
  }

  def getUserByEmail(username: String): User = {
    userPersistence.findByEmail(username)
  }
}

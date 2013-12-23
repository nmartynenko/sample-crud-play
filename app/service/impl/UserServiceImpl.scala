package service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.{NoOpPasswordEncoder, PasswordEncoder}
import org.springframework.stereotype.Service
import persistence.UserPersistence
import model.impl.User
import service.UserService


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

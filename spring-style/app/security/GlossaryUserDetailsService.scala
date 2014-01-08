package security

import java.util
import models.UserRole
import models.impl.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.{UsernameNotFoundException, UserDetails, UserDetailsService}
import scala._
import services.UserService

class GlossaryUserDetailsService extends UserDetailsService {

  @Autowired
  private val userService: UserService = null

  @throws[UsernameNotFoundException]
  def loadUserByUsername(username: String): UserDetails = {

    val user = userService.getUserByEmail(username)

    user match {
      case null =>
        null
      case _ =>
        val userDetails = GlossaryUserDetails(
          username, user.password, getGrantedAuthorities(user)
        )

        //set actual DB user for possible further purposes
        userDetails.user = user

        userDetails
    }
  }


  private def getGrantedAuthorities(user: User) = {
    var userRoles = List(UserRole.USER)

    if (user.role == UserRole.ADMIN){
      userRoles = UserRole.ADMIN :: userRoles
    }

    val grantedAuthorities = new util.ArrayList[GrantedAuthority](userRoles.size)

    for (authority <- userRoles) {
      grantedAuthorities.add(new SimpleGrantedAuthority(authority.toString))
    }

    grantedAuthorities
  }
}

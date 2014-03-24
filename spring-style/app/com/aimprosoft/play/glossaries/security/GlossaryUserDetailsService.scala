package com.aimprosoft.play.glossaries.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.{UsernameNotFoundException, UserDetails, UserDetailsService}
import com.aimprosoft.play.glossaries.services.UserService
import com.aimprosoft.play.glossaries.models.impl.User
import com.aimprosoft.play.glossaries.models.UserRole

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
    val userRoles = user.role match {
      case UserRole.ADMIN =>
        GlossaryUserDetailsService.ADMIN_ROLES
      case UserRole.USER =>
        GlossaryUserDetailsService.USER_ROLES
      case _ =>
        Nil
    }

    userRoles map {role =>
      new SimpleGrantedAuthority(role.toString)
    }
  }
}

//set of constants
object GlossaryUserDetailsService{
  val ADMIN_ROLES = List(UserRole.USER, UserRole.ADMIN)
  val USER_ROLES = List(UserRole.USER)
}
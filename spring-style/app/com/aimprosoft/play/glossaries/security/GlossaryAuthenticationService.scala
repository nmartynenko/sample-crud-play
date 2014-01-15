package com.aimprosoft.play.glossaries.security

import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.{Authentication, AuthenticationException}
import org.springframework.stereotype.Service
import play.api.Play.{current => app}

@Service
class GlossaryAuthenticationService {

  @Autowired
  private val authenticationProvider: DaoAuthenticationProvider = null

  def authenticate(username: String, password: String): Option[Authentication] = {
    try {
      Some(authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password)))
    }
    catch {
      case _ : AuthenticationException => None
    }
  }

  def generateIdentifier(username: String) = {
    DigestUtils.shaHex(app.hashCode + username)
  }

}

package com.aimprosoft.play.glossaries.security

import be.objectify.deadbolt.core.models.Subject
import com.aimprosoft.play.glossaries.exceptions.NoUserFoundException
import com.aimprosoft.play.glossaries.services.UserService
import org.mindrot.jbcrypt.BCrypt

trait SecurityUserService {
  def authenticate(username: String, password: String): Option[Subject]
}

class DeadboltSecurityUserService extends SecurityUserService{


  def authenticate(username: String, password: String): Option[Subject] = {
    try {
      val user = UserService.getUserByEmail(username)

      //if password doesn't match
      if (!BCrypt.checkpw(password, user.password)) None
      else {
        Some(new GlossaryUserSubject(user))
      }
    }
    catch {
      case e: NoUserFoundException => None
    }
  }
}

object SecurityUserService extends DeadboltSecurityUserService
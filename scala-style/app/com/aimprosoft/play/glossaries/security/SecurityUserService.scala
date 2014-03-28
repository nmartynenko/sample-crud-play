package com.aimprosoft.play.glossaries.security

import be.objectify.deadbolt.core.models.Subject
import com.aimprosoft.play.glossaries.service.UserService
import org.mindrot.jbcrypt.BCrypt

trait SecurityUserService {
  def authenticate(username: String, password: String): Option[Subject]
}

class DeadboltSecurityUserService extends SecurityUserService{

  def authenticate(username: String, password: String): Option[Subject] = {
    UserService.getByEmail(username) match {
      case Some(user) =>
        //if password doesn't match
        if (!BCrypt.checkpw(password, user.password))
          None
        else
          Some(new GlossaryUserSubject(user))

      case _ =>
        None
    }
  }
}

object SecurityUserService extends DeadboltSecurityUserService
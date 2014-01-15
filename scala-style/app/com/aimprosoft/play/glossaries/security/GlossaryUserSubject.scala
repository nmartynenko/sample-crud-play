package com.aimprosoft.play.glossaries.security

import be.objectify.deadbolt.core.models.{Role, Permission, Subject}
import com.aimprosoft.play.glossaries.models.User
import java.util
import org.apache.commons.codec.digest.DigestUtils
import play.api.Play.{current => app}

class GlossaryUserSubject(val user: User) extends Subject with Serializable{
  //Java2Scala conversions and vice versa
  import scala.collection.JavaConversions._

  private val roles: util.List[SimpleRole] = GlossaryRoles(user.role)

  def getRoles: util.List[_ <: Role] = roles

  def getPermissions: util.List[_ <: Permission] = util.Collections.emptyList()

  def getIdentifier: String = user.email

  override def toString: String = user.name
}

object GlossaryUserSubject{
  def generateIdentifier(username: String): String = {
    DigestUtils.shaHex(app.hashCode() + username)
  }
}
package security

import be.objectify.deadbolt.core.models.{Role, Permission, Subject}
import play.api.Play.{current => app}
import java.util
import models.User
import org.apache.commons.codec.digest.DigestUtils

//Java2Scala conversions and vice versa
import scala.collection.JavaConversions._

class GlossaryUserSubject(user: User) extends Subject with Serializable{
  private val roles: util.List[_ <: Role] = GlossaryRoles(user.role)

  private val identifier: String = GlossaryUserSubject.generateIdentifier(user.email)

  def getRoles: util.List[_ <: Role] = roles

  def getPermissions: util.List[_ <: Permission] = util.Collections.emptyList()

  def getIdentifier: String = identifier

  override def toString: String = s"GlossaryUserSubject($identifier)"
}

object GlossaryUserSubject{
  def generateIdentifier(username: String): String = {
    DigestUtils.shaHex(app.hashCode() + username)
  }
}
package security

import be.objectify.deadbolt.core.models.{Role, Permission, Subject}
import java.util
import models.User

//Java2Scala conversions and vice versa
import scala.collection.JavaConversions._

class GlossaryUserSubject(user: User) extends Subject{
  private val roles: util.List[_ <: Role] = GlossaryRoles(user.role)

  private val identifier: String = GlossaryUserSubject.generateIdentifier(user.email)

  def getRoles: util.List[_ <: Role] = roles

  def getPermissions: util.List[_ <: Permission] = util.Collections.emptyList()

  def getIdentifier: String = identifier
}

object GlossaryUserSubject{
  def generateIdentifier(username: String): String = {
    //todo use more secured way to generate an identifier
    username
  }
}
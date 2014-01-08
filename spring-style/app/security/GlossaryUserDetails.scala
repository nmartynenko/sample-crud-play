package security

import java.util
import org.springframework.security.core.GrantedAuthority
import scala.beans.BeanProperty
import models.impl.User

case class GlossaryUserDetails(username: String,
                               password: String,
                               authorities: util.Collection[_ <: GrantedAuthority])
  extends org.springframework.security.core.userdetails.User(username, password, authorities) {

  @BeanProperty
  var user: User = _
}
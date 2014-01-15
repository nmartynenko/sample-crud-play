package com.aimprosoft.play.glossaries.security

import com.aimprosoft.play.glossaries.models.impl.User
import org.springframework.security.core.GrantedAuthority
import scala.beans.BeanProperty

//Java2Scala conversions and vice versa
import scala.collection.JavaConversions._

case class GlossaryUserDetails(username: String,
                               password: String,
                               authorities: List[_ <: GrantedAuthority])
  extends org.springframework.security.core.userdetails.User(username, password, authorities) {

  @BeanProperty
  var user: User = _
}

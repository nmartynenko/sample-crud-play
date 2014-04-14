package com.aimprosoft.play.glossaries.models.impl

import com.aimprosoft.play.glossaries.models.{UserRole, BusinessModel}
import javax.persistence._
import scala.beans.BeanProperty

@Entity
@Table(name = "glossary_user")
class User extends BusinessModel with Cloneable{

  @BeanProperty
  //hibernate
  @Column(name = "email", nullable = false, unique = true)
  var email: String = _

  @BeanProperty
  //hibernate
  @Column(name = "password", nullable = false, unique = true)
  var password: String = _

  @BeanProperty
  //hibernate
  @Column(name = "name", nullable = false)
  @BeanProperty
  var name: String = _

  @BeanProperty
  @Enumerated(EnumType.STRING)
  var role: UserRole = _

  def canEqual(other: Any): Boolean = other.isInstanceOf[User]

  override def equals(other: Any): Boolean = other match {
    case that: User =>
      (that canEqual this) &&
        email == that.email &&
        password == that.password &&
        name == that.name &&
        role == that.role
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(email, password, name, role)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def clone(): AnyRef = super.clone()
}
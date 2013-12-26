package models.impl

import javax.persistence._
import scala.beans.BeanProperty
import models.BusinessModel


@Entity
@Table(name = "glossary_user")
class User extends BusinessModel {

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
  var role: String = _
/*
  @Enumerated(EnumType.STRING)
  var role: UserRole = _
*/

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
}
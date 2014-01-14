package models.impl

import javax.persistence._
import models.BusinessModel
import scala.beans.BeanProperty
import net.sf.oval.constraint.{NotEmpty, NotNull}

@Entity
@Table(name = "glossary")
class Glossary extends BusinessModel {

  @BeanProperty
  //validation
  @NotNull(message = "sample.error.not.null")
  @NotEmpty(message = "sample.error.not.empty")
  //hibernate
  @Column(name = "name", nullable = false, unique = true)
  var name: String = _

  @BeanProperty
  //validation
  @NotNull(message = "sample.error.not.null")
  @NotEmpty(message = "sample.error.not.empty")
  //hibernate
  @Lob
  @Column(name = "description", nullable = true, length = 4096)
  var description: String = _

  def canEqual(other: Any): Boolean = other.isInstanceOf[Glossary]

  override def equals(other: Any): Boolean = other match {
    case that: Glossary =>
      (that canEqual this) &&
        name == that.name &&
        description == that.description
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(name, description)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}

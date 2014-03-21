package com.aimprosoft.play.glossaries.persistence

import play.api.db.slick.Config.driver.simple._
import com.aimprosoft.play.glossaries.models.{User, Glossary}
import com.aimprosoft.play.glossaries.exceptions.NoUserFoundException

//application DAO
trait GlossaryPersistence extends Persistence[Glossary, Long]

trait UserPersistence extends Persistence[User, Long] {
  def findByEmail(email: String)(implicit session: Session): User
}

//application DAO objects
object GlossaryPersistence extends SlickGlossariesPersistence

object UserPersistence extends SlickUsersPersistence

//DB Table descriptions
class SlickGlossaries(tag: Tag) extends SlickBaseTable[Glossary, Long](tag, "glossary") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name", O.NotNull)

  def description = column[String]("description", O.Nullable)

  def * = (id.?, name, description.?) <> (Glossary.tupled, Glossary.unapply)
}

class SlickUsers(tag: Tag) extends SlickBaseTable[User, Long](tag, "glossary_user") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def email = column[String]("email", O.NotNull)

  def password = column[String]("password", O.Nullable)

  def name = column[String]("name", O.NotNull)

  def role = column[String]("role")

  def * = (id.?, email, password, name, role) <> (User.tupled, User.unapply)

}

//concrete persistence services
class SlickUsersPersistence extends SlickBasePersistence[User, Long, SlickUsers]
  with UserPersistence{

  val tableQuery = TableQuery[SlickUsers]

  def findByEmail(email: String)(implicit session: Session) = {
    tableQuery.filter(_.email === email).firstOption getOrElse {
      throw new NoUserFoundException(username = email)
    }
  }
}

class SlickGlossariesPersistence extends SlickBasePersistence[Glossary, Long, SlickGlossaries]
  with GlossaryPersistence {
  val tableQuery = TableQuery[SlickGlossaries]
}
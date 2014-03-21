package com.aimprosoft.play.glossaries.persistence

import com.aimprosoft.play.glossaries.exceptions.NoUserFoundException
import play.api.db.slick.Config.driver.simple._
import com.aimprosoft.play.glossaries.models.Glossary
import com.aimprosoft.play.glossaries.models.User

trait Persistence[T, ID] {

  def get(id: ID)(implicit session: Session): Option[T]

  def list()(implicit session: Session): Seq[T]

  def list(startRow: Int = -1, pageSize: Int = -1)(implicit session: Session): Seq[T]

  def insert(entity: T)(implicit session: Session): Long

  def insertAll(entities: Seq[T])(implicit session: Session): Unit

  def update(entity: T)(implicit session: Session): Unit

  def delete(id: ID)(implicit session: Session): Unit

  def count(implicit session: Session): Int
}

//application DAO
trait GlossaryPersistence extends Persistence[Glossary, Long]

trait UserPersistence extends Persistence[User, Long] {
  def findByEmail(email: String)(implicit session: Session): User
}

//application DAO objects
object GlossaryPersistence extends SlickGlossariesPersistence

object UserPersistence extends SlickUsersPersistence

abstract class SlickBaseTable[T](tag: Tag, tableName: String) extends Table[T](tag, tableName){
  def id: Column[Long]
}

trait SlickBasePersistence[T <: {val id: Option[Long]}, TQ <: SlickBaseTable[T]] extends Persistence[T, Long] {

  val tableQuery: TableQuery[TQ]

  protected def byId(id: Long)(implicit session: Session): Option[T] = tableQuery.filter( _.id === id).firstOption

  protected def autoInc = tableQuery returning tableQuery.map(_.id)

  def get(id: Long)(implicit session: Session): Option[T] = byId(id)

  def list()(implicit session: Session): Seq[T] = {
    list(-1, -1)
  }

  def list(startRow: Int, pageSize: Int)(implicit session: Session): Seq[T] = {
    //create query for retrieving of all entities
    var q = tableQuery.map(e => e)

    //if it needs to be started from certain row
    if (startRow > 0){
      q = q.drop(startRow)
    }

    //if we need to get only certain number of rows
    if (pageSize >= 0) {
      q = q.take(pageSize)
    }

    //perform query
    q.list()
  }

  def insert(entity: T)(implicit session: Session) = {
    autoInc.insert(entity)
  }

  def insertAll(entities: Seq[T])(implicit session: Session) {
    autoInc.insertAll(entities: _*)
  }

  def update(entity: T)(implicit session: Session) {
    tableQuery.filter(_.id === entity.id).update(entity)
  }

  def delete(id: Long)(implicit session: Session) {
    tableQuery.filter(_.id === id).delete
  }

  def count(implicit session: Session) = {
    tableQuery.length.run
  }

}

//DB Models
class SlickGlossaries(tag: Tag) extends SlickBaseTable[Glossary](tag, "glossary") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name", O.NotNull)

  def description = column[String]("description", O.Nullable)

  def * = (id.?, name, description.?) <> (Glossary.tupled, Glossary.unapply)
}

class SlickUsers(tag: Tag) extends SlickBaseTable[User](tag, "glossary_user") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def email = column[String]("email", O.NotNull)

  def password = column[String]("password", O.Nullable)

  def name = column[String]("name", O.NotNull)

  def role = column[String]("role")

  def * = (id.?, email, password, name, role) <> (User.tupled, User.unapply)

}

class SlickUsersPersistence extends SlickBasePersistence[User, SlickUsers] with UserPersistence{
  val tableQuery = TableQuery[SlickUsers]

  def findByEmail(email: String)(implicit session: Session) = {
    tableQuery.filter(_.email === email).firstOption getOrElse {
        throw new NoUserFoundException(username = email)
    }
  }
}

class SlickGlossariesPersistence extends SlickBasePersistence[Glossary, SlickGlossaries] with GlossaryPersistence {
  val tableQuery = TableQuery[SlickGlossaries]
}
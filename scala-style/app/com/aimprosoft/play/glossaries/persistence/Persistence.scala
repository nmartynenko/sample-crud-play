package com.aimprosoft.play.glossaries.persistence

import com.aimprosoft.play.glossaries.exceptions.{NoUserFoundException, NoGlossaryFoundException}
import com.aimprosoft.play.glossaries.models._
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import scala.slick.driver.ExtendedDriver
import scala.slick.lifted

trait Persistence[T <: AnyRef {val id : Option[Long]}] {

  def get(id: Long)(implicit session: Session): Option[T]

  def list()(implicit session: Session): Seq[T]

  def list(startRow: Int = -1, pageSize: Int = -1)(implicit session: Session): Seq[T]

  def insert(entity: T)(implicit session: Session): Long

  def insertAll(entities: Seq[T])(implicit session: Session): Unit

  def update(id: Long, entity: T)(implicit session: Session): Unit

  def delete(id: Long)(implicit session: Session): Unit

  def count(implicit session: Session): Int
}

//application DAO
trait GlossaryPersistence extends Persistence[Glossary]

trait UserPersistence extends Persistence[User] {
  def findByEmail(email: String)(implicit session: Session): User
}

//application DAO objects
object GlossaryPersistence extends SlickGlossaries

object UserPersistence extends SlickUsers

/**
 * Helper for otherwise verbose Slick model definitions
 *
 * @author Manuel Bernhardt
 * @url http://manuel.bernhardt.io/2013/07/08/crud-trait-for-slick-models-in-the-play-framework/
 *
 */
trait SlickBaseModel[T <: AnyRef {val id : Option[Long]}] extends Persistence[T] {
  self: Table[T] =>

  val byId = createFinderBy( t => t.id )

  def id: Column[Long]

  def * : scala.slick.lifted.ColumnBase[T]

  def autoInc = * returning id

  def get(id: Long)(implicit session: Session): Option[T] = byId(id).firstOption

  def list()(implicit session: Session): Seq[T] = {
    list(-1, -1)
  }

  def list(startRow: Int, pageSize: Int)(implicit session: Session): Seq[T] = {
    //create query for retrieving of all entities
    var q = tableToQuery(self).map(t => t)

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

  def update(id: Long, entity: T)(implicit session: Session) {
    tableQueryToUpdateInvoker(
      tableToQuery(self).where(_.id === id)
    ).update(entity)
  }

  def delete(id: Long)(implicit session: Session) {
    queryToDeleteInvoker(
      tableToQuery(self).where(_.id === id)
    ).delete
  }

  def count(implicit session: Session) = {
      Query(tableToQuery(self).length).first
  }

}

//DB Models
class SlickGlossaries extends Table[Glossary]("glossary")
  with GlossaryPersistence
  with SlickBaseModel[Glossary] {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name", O.NotNull)

  def description = column[String]("description")

  def * = id.? ~ name ~ description.? <> (Glossary.apply _, Glossary.unapply _)
}

class SlickUsers extends Table[User]("glossary_user")
  with UserPersistence
  with SlickBaseModel[User] {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def email = column[String]("email", O.NotNull)

  def password = column[String]("password")

  def name = column[String]("name", O.NotNull)

  def role = column[String]("role")

  def * = id.? ~ email ~ password ~ name ~ role <> (User.apply _, User.unapply _)

  val byEmail = createFinderBy( t => t.email )

  def findByEmail(email: String)(implicit session: Session): User = {
    byEmail(email).firstOption getOrElse {
      throw new NoUserFoundException(username = email)
    }
  }
}
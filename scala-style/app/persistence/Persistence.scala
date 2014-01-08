package persistence

import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import models._
import scala.slick.driver.ExtendedDriver
import exceptions.{NoUserFoundException, NoGlossaryFoundException}
import scala.slick.lifted

trait Persistence[T <: AnyRef {val id : Option[Long]}] {

  def ddlCreate(): Unit

  def get(id: Long)(implicit session: Session): T

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
object GlossaryPersistence extends SlickGlossaries with GlossaryPersistence

object UserPersistence extends SlickUsers with UserPersistence

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

  def ddlCreate(): Unit = DB.withSession {implicit session: Session =>
    self.ddl.create
  }

  def id: Column[Long]

  def * : scala.slick.lifted.ColumnBase[T]

  def autoInc = * returning id

  def get(id: Long)(implicit session: Session): T = byId(id).first

  def list()(implicit session: Session): Seq[T] = {
    list(-1, -1)
  }

  def list(startRow: Int, pageSize: Int)(implicit session: Session): Seq[T] = {
    //create query for retrieving of all entities
    var q = tableToQuery(this).map(t => t)

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
      tableToQuery(this).where(_.id === id)
    ).update(entity)
  }

  def delete(id: Long)(implicit session: Session) {
    queryToDeleteInvoker(
      tableToQuery(this).where(_.id === id)
    ).delete
  }

  def count(implicit session: Session) = {
      Query(tableToQuery(this).length).first
  }

}

//DB Models
class SlickGlossaries extends Table[Glossary]("glossary")
  with GlossaryPersistence
  with SlickBaseModel[Glossary] {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name", O.NotNull)

  def description = column[String]("description")

  def * = id.? ~ name ~ description <> (Glossary.apply _, Glossary.unapply _)
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
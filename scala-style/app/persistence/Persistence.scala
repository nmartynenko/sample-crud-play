package persistence

import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import models._
import scala.slick.driver.ExtendedDriver

trait Persistence[T <: AnyRef {val id : Long}] {

  def get(id: Long): T

  def list(startRow: Int = -1, pageSize: Int = -1): Seq[T]

  def insert(entity: T): Long

  def insertAll(entities: Seq[T]): Unit

  def update(id: Long, entity: T): Unit

  def delete(id: Long): Unit

  def count: Int
}

trait GlossaryPersistence extends Persistence[Glossary]

trait UserPersistence extends Persistence[User]

object GlossaryPersistence extends SlickGlossaries with GlossaryPersistence

object UserPersistence extends SlickUsers with UserPersistence

/**
 * Helper for otherwise verbose Slick model definitions
 *
 * @author Manuel Bernhardt
 * @url http://manuel.bernhardt.io/2013/07/08/crud-trait-for-slick-models-in-the-play-framework/
 *
 */
trait BasePersistence[T <: AnyRef {val id : Long}] extends Persistence[T] {
  self: Table[T] =>

  def id: Column[Long]

  def * : scala.slick.lifted.ColumnBase[T]

  def autoInc = * returning id


  def get(id: Long): T = {
    DB.withSession { implicit session: Session =>
      ???
    }
  }

  def list(startRow: Int, pageSize: Int): T = {
    DB.withSession { implicit session: Session =>
      ???
    }
  }

  def insert(entity: T) = {
    DB.withTransaction { implicit session: Session =>
        autoInc.insert(entity)
    }
  }

  def insertAll(entities: Seq[T]) {
    DB.withTransaction { implicit session: Session =>
        autoInc.insertAll(entities: _*)
    }
  }

  def update(id: Long, entity: T) {
    DB.withTransaction { implicit session: Session =>
      tableQueryToUpdateInvoker(
          tableToQuery(this).where(_.id === id)
        ).update(entity)
    }
  }

  def delete(id: Long) {
    DB.withTransaction { implicit session: Session =>
        queryToDeleteInvoker(
          tableToQuery(this).where(_.id === id)
        ).delete
    }
  }

  def count = DB.withTransaction { implicit session: Session =>
      Query(tableToQuery(this).length).first
  }

}

//DB Models
class SlickGlossaries extends Table[Glossary]("glossary") with BasePersistence[Glossary] {
  def id = column[Long]("id", O.PrimaryKey)

  def name = column[String]("name", O.NotNull)

  def description = column[String]("description")

  def * = id ~ name ~ description <>(Glossary.apply _, Glossary.unapply _)
}

class SlickUsers extends Table[User]("glossary_user") with BasePersistence[User] {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def email = column[String]("email", O.NotNull)

  def password = column[String]("password")

  def name = column[String]("name", O.NotNull)

  def role = column[String]("role")

  def * = id ~ email ~ password ~ name ~ role <>(User.apply _, User.unapply _)
}
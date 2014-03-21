package com.aimprosoft.play.glossaries.persistence

import play.api.db.slick.Config.driver.simple._

//abstract persistence trait
trait Persistence[T, ID] {

  def get(id: ID)(implicit session: Session): Option[T]

  def list()(implicit session: Session): Seq[T]

  def list(startRow: Int = -1, pageSize: Int = -1)(implicit session: Session): Seq[T]

  def insert(entity: T)(implicit session: Session): ID

  def insertAll(entities: Seq[T])(implicit session: Session): Unit

  def update(entity: T)(implicit session: Session): Unit

  def delete(id: ID)(implicit session: Session): Unit

  def count(implicit session: Session): Int
}

//abstract
abstract class SlickBaseTable[T, ID](tag: Tag, tableName: String) extends Table[T](tag, tableName){
  def id: Column[ID]
}

trait SlickBasePersistence[T <: {val id: Option[ID]}, ID, TQ <: SlickBaseTable[T, ID]] extends Persistence[T, ID] {

  val tableQuery: TableQuery[TQ]

  protected def byId(id: ID)(implicit session: Session): Option[T] = tableQuery.filter(_.id === id).firstOption

  protected def autoInc = tableQuery returning tableQuery.map(_.id)

  def get(id: ID)(implicit session: Session): Option[T] = byId(id)

  def list()(implicit session: Session): Seq[T] = {
    list(-1, -1)
  }

  def list(startRow: Int, pageSize: Int)(implicit session: Session): Seq[T] = {
    //create query for retrieving of all entities
    var q = tableQuery.map(e => e)

    //if it needs to be started from certain row
    if (startRow > 0) {
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

  def delete(id: ID)(implicit session: Session) {
    tableQuery.filter(_.id === id).delete
  }

  def count(implicit session: Session) = {
    tableQuery.length.run
  }

}
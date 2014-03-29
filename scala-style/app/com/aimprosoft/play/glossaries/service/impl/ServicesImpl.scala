package com.aimprosoft.play.glossaries.service.impl

import com.aimprosoft.play.glossaries.domain.PageResponse
import com.aimprosoft.play.glossaries.models.{User, Glossary}
import com.aimprosoft.play.glossaries.persistence._
import com.aimprosoft.play.glossaries.service._
import play.api.Play.current
import play.api.db.slick._
import scala.language.reflectiveCalls
import org.mindrot.jbcrypt.BCrypt

trait SlickTransactional {

  def readOnly[T]: (Session => T) => T = DB.withSession

  def transactional[T]: (Session => T) => T = DB.withTransaction

}

trait BaseCrudServiceImpl[T <: {val id: Option[Long]}, P <: Persistence[T, Long]] extends BaseCrudService[T]
  with SlickTransactional{

  def persistence: P

  def getCurrentPage(startRow: Int, pageSize: Int): PageResponse[T] = readOnly {
    implicit session: Session => {
      //list of entities
      val content = persistence.list(startRow, pageSize)

      //number of all elements in DB
      val te = {
        //quick check for not working with DB
        if (startRow <= 0 && pageSize < 0)
          content.size
        //check in DB otherwise
        else
          persistence.count
      }

      //adjust start row
      val sr = if (startRow < 0) 0 else startRow
      //adjust page size
      val ps = if (pageSize > 0) pageSize else te

      new PageResponse(content, sr, ps, te)
    }
  }


  def exists(id: Long): Boolean = readOnly {
    implicit session: Session =>
      persistence.exists(id)
  }

  def count: Int = readOnly {
    implicit session: Session =>
      persistence.count
  }

  def getFirst: Option[T] = readOnly {
    implicit session: Session =>
      persistence.list(0, 1).headOption
  }

  def getById(id: Long): Option[T] = readOnly {
    implicit session: Session =>
      persistence.get(id)
  }

  def add(entity: T): Unit = transactional {
    implicit session: Session =>
      persistence.insert(entity)
  }

  def update(entity: T): Unit = transactional {
    implicit session: Session =>
      persistence.update(entity)
  }

  def remove(entity: T): Unit = entity.id foreach { id =>
    removeById(id)
  }

  def removeById(id: Long): Unit = transactional {
    implicit session: Session =>
      persistence.delete(id)
  }

}

class GlossaryServiceImpl extends GlossaryService
  with BaseCrudServiceImpl[Glossary, GlossaryPersistence] {
  def persistence = GlossaryPersistence
}

class UserServiceImpl extends UserService
  with BaseCrudServiceImpl[User, UserPersistence] {

  def persistence = UserPersistence

  override def add(user: User): Unit = {
    //hash plain text password
    val hashedPassword = BCrypt.hashpw(user.password, BCrypt.gensalt())

    super.add(user.copy(password = hashedPassword))
  }

  def getByEmail(email: String): Option[User] = transactional {
    implicit session: Session =>
      UserPersistence.findByEmail(email)
  }

}
package com.aimprosoft.play.glossaries.service

import be.objectify.deadbolt.core.models.Role
import com.aimprosoft.play.glossaries.domain.PageResponse
import com.aimprosoft.play.glossaries.models.{User, Glossary}
import com.aimprosoft.play.glossaries.service.impl.{UserServiceImpl, GlossaryServiceImpl}

trait BaseCrudService[T] {

  def getCurrentPage(startRow: Int, pageSize: Int): PageResponse[T]

  def exists(glossaryId: Long): Boolean

  def count: Int

  def getFirst: Option[T]

  def getById(glossaryId: Long): Option[T]

  def add(glossary: T): Unit

  def update(glossary: T): Unit

  def remove(glossary: T): Unit

  def removeById(glossaryId: Long): Unit
}

trait GlossaryService extends BaseCrudService[Glossary]

trait UserService extends BaseCrudService[User]{

  def getByEmail(username: String): Option[User]

  def countByRole(role: Role): Long

}

object GlossaryService extends GlossaryServiceImpl

object UserService extends UserServiceImpl


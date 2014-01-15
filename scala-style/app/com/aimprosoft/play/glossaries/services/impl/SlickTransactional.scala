package com.aimprosoft.play.glossaries.services.impl

import play.api.Play.current
import play.api.db.slick.{DB, Session}

trait SlickTransactional {

  def readOnly[T]: (Session => T) => T = DB.withSession

  def transactional[T]: (Session => T) => T = DB.withTransaction

}

package services.impl

import play.api.Play.current
import play.api.db.slick.{Session, DB}

trait SlickTransactional {

  def readOnly[T]: ((Session) => T) => T = DB.withSession

  def transactional[T]: ((Session) => T) => T = DB.withTransaction

}

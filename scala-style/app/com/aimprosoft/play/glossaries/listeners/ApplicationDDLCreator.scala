package com.aimprosoft.play.glossaries.listeners

import com.aimprosoft.play.glossaries.persistence.{GlossaryPersistence, UserPersistence}
import play.api.Logger
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import scala.slick.jdbc.meta.MTable

object ApplicationDDLCreator extends Listener{
  def init(): Unit = {
    DB.withSession {implicit session: Session =>
      if (needsDdlCreation){
        Logger.info("Start updating DDL for application")
        (UserPersistence.ddl ++ GlossaryPersistence.ddl).create
        Logger.info("Updating DDL for application has ended")
      }
    }
  }

  private def needsDdlCreation(implicit session: Session): Boolean = {
    MTable.getTables.list().isEmpty
  }
}

package com.aimprosoft.play.glossaries.listeners

import com.aimprosoft.play.glossaries.persistence.{GlossaryPersistence, UserPersistence}
import play.api.Logger
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB

object ApplicationDDLCreator extends Listener{

  def init() {
    DB.withSession {implicit session: Session =>
      //check whether we need to create DDL
      if (needsDdlCreation){
        Logger.info("Start updating DDL for application")
        //perform DB schema creation
        createDdl

        Logger.info("Updating DDL for application has ended")
      }
    }
  }

  //specific implementations
  private def createDdl(implicit session: Session) {
    (UserPersistence.tableQuery.ddl ++ GlossaryPersistence.tableQuery.ddl).create
  }

  @inline
  private def needsDdlCreation(implicit session: Session): Boolean = {
    slickDriver.getTables.list.isEmpty
  }
}

package com.aimprosoft.play.glossaries.mapper

import play.api.Logger
import play.api.mvc.Results._
import play.api.mvc._
import scala.concurrent.Future

//set of responses for Play's global error handling
object ErrorResponses {

  def handleException(ex: Exception, request: RequestHeader): Future[Result] = {
    Logger.error(ex.getMessage, ex)

    Future.successful(InternalServerError(ex.getMessage))
  }

}

package controllers

import be.objectify.deadbolt.scala.DeadboltActions
import play.api.mvc._
import security._

trait SecuredController extends Controller with DeadboltActions {
  protected val HOME = Redirect("/index.html")

  def authenticated[T]: (Action[T]) => Action[T] = {
    SubjectPresent(SubjectPresentGlossaryUserDeadboltHandler)
  }

  def notAuthenticated[T]: (Action[T]) => Action[T] = {
    SubjectNotPresent(SubjectNotPresentGlossaryUserDeadboltHandler)
  }

  def asAdmin[T]: (Action[T]) => Action[T] = {
    Restrict(Array("admin"), SubjectPresentGlossaryUserDeadboltHandler)
  }

}

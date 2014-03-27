package controllers

import be.objectify.deadbolt.scala.DeadboltActions
import com.aimprosoft.play.glossaries.security._
import play.api.mvc._

trait SecuredController extends Controller with DeadboltActions {
  protected val HOME = Redirect("/index.html")

  def authenticated[T]: (Action[T]) => Action[T] = SubjectPresent(SubjectPresentGlossaryUserDeadboltHandler)

  def notAuthenticated[T]: (Action[T]) => Action[T] = SubjectNotPresent(SubjectNotPresentGlossaryUserDeadboltHandler)

  def asAdmin[T]: (Action[T]) => Action[T] = Restrict(Array("admin"), SubjectPresentGlossaryUserDeadboltHandler)

}

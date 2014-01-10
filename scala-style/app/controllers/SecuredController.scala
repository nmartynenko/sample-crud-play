package controllers

import be.objectify.deadbolt.scala.DeadboltActions
import play.api.mvc.Controller

trait SecuredController extends Controller with DeadboltActions {
  protected val HOME = Redirect("/index.html")
}

package controllers

import play.api.mvc._
import service.GlossaryService
import org.springframework.beans.factory.annotation.Autowired

@org.springframework.stereotype.Controller
class HtmlController extends Controller {

  @Autowired
  private val glossaryService: GlossaryService = null

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}
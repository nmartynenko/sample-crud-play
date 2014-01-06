package controllers

import play.api.mvc.Controller
import org.springframework.beans.factory.annotation.Autowired
import com.fasterxml.jackson.databind.ObjectMapper

abstract class BaseController extends Controller {

  @Autowired
  protected val objectMapper: ObjectMapper = null

}

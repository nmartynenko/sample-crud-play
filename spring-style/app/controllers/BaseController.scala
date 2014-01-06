package controllers

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import play.api.mvc.Controller

abstract class BaseController extends Controller {

  @Autowired
  protected val objectMapper: ObjectMapper = null

}

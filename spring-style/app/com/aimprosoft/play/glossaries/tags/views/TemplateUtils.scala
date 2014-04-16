package com.aimprosoft.play.glossaries.tags.views

import com.aimprosoft.play.glossaries.security.PlaySecurityHolder
import play.api.mvc.Request

trait TemplateUtils {

  def principal[T](implicit request: Request[_]): Option[T] = {
    PlaySecurityHolder.getAuthentication map { auth =>
      auth.getPrincipal.asInstanceOf[T]
    }
  }

}

object TemplateUtils extends TemplateUtils
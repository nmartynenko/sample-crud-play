package com.aimprosoft.play.glossaries

import com.aimprosoft.play.glossaries.security.PlaySecurityHolder
import org.springframework.security.core.context.SecurityContextHolder
import play.api.mvc.RequestHeader

trait SecurityInterceptor {

  def setAuth(implicit request: RequestHeader): Unit = {
    PlaySecurityHolder.getAuthentication match {
      case Some(auth) =>
        SecurityContextHolder.getContext.setAuthentication(auth)
      case _ =>
        SecurityContextHolder.clearContext()
    }
  }

}

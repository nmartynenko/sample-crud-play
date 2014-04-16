package com.aimprosoft.play.glossaries.tags

import com.aimprosoft.play.glossaries.models.UserRole
import com.aimprosoft.play.glossaries.security.PlaySecurityHolder
import play.api.mvc.Request

trait SpringSecurityViewSupport {

  import scala.collection.JavaConversions._

  def hasRole(roles: UserRole*)(implicit request: Request[_]): Boolean = {
    PlaySecurityHolder.getAuthentication exists {
      auth =>
        auth.getAuthorities exists { authority =>
          roles exists { role =>
            role.name == authority.getAuthority
          }
        }
    }
  }

}

object SpringSecurityViewSupport extends SpringSecurityViewSupport
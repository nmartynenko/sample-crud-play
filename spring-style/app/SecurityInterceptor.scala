import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import play.api.Logger
import play.api.Play.current
import play.api.cache.Cache
import play.api.mvc.{Security, RequestHeader}

trait SecurityInterceptor {

  def setAuth(request: RequestHeader): Unit = {
    //get username from session
    request.session.get(Security.username) match {
      //if nothing found
      case Some(username) =>
        val fromCache = Cache.getAs[Authentication](username)

        Logger.debug(s"Getting value for $username, and it returns $fromCache")

        fromCache match {
          case Some(auth: Authentication) =>
            //set auth into security context
            SecurityContextHolder.getContext.setAuthentication(auth)
          case _ => ()
        }
      //otherwise do nothing
      case _ => ()
    }

  }

}

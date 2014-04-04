import com.aimprosoft.play.glossaries.models.impl.Glossary
import com.aimprosoft.play.glossaries.security.GlossaryUserDetailsService
import com.aimprosoft.play.glossaries.SpringAwareGlobalSetting
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import play.api.cache.Cache
import play.api.http.HeaderNames
import play.api.libs.json.JsObject
import play.api.mvc.Security
import play.api.test.Helpers._
import play.api.test._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "redirect from / into login.html" in new WithApplication {
      var rootRoute = route(FakeRequest(GET, "/")).get

      status(rootRoute) must equalTo(SEE_OTHER)
      headers(rootRoute) must havePair(HeaderNames.LOCATION -> "/login.html")
    }

    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/boum")) must beNone
    }

  }

  "Application for unauthenticated users" should {

    "show login page" in new WithApplication{
      val loginUrl = route(
        FakeRequest(GET, "/login.html")
          //for POST/PUT requests, for others it's ignored either way
          .withJsonBody(JsObject(List()))
      ).get

      status(loginUrl) must equalTo(OK)
      contentType(loginUrl) must beSome.which(_ == "text/html")

      val content: String = contentAsString(loginUrl)

      Seq("form", "j_username", "j_password", "submit") foreach {
        term =>
          content must contain(term)
      }
    }

    "do not show all other pages/routes" in new WithApplication {
      Seq(
        ("/index.html", GET),
        ("/glossaries", GET),
        ("/glossaries/1", GET),
        ("/glossaries", PUT),
        ("/glossaries", POST),
        ("/glossaries/1", DELETE),
        ("/logout", GET)
      ) foreach {

        case (url, method) =>
          val forbiddenRoute = route(
            FakeRequest(method, url)
              //for POST/PUT requests, for others it's ignored either way
              .withJsonBody(JsObject(List()))
          ).get

          status(forbiddenRoute) must equalTo(SEE_OTHER)
          headers(forbiddenRoute) must havePair(HeaderNames.LOCATION -> "/login.html")
      }
    }

  }

  "Application for authenticated users" should {
    import scala.collection.JavaConversions._

    val adminUsername: String = "fakeAdmin"
    val adminAuth: Authentication = new UsernamePasswordAuthenticationToken(
      adminUsername, "", GlossaryUserDetailsService.ADMIN_AUTHORITIES
    )
    val userUsername: String = "fakeUser"
    val userAuth: Authentication = new UsernamePasswordAuthenticationToken(
      userUsername, "", GlossaryUserDetailsService.USER_AUTHORITIES
    )
    val users: Seq[(String, Authentication)] = Seq(
      (adminUsername, adminAuth),
      (userUsername, userAuth)
    )

    def forAllUsers(f: => (String, Authentication) => Unit): Unit = {
      users foreach {
        case (username, auth) => f(username, auth)
      }
    }

    def authenticateAllUsers(implicit app: FakeApplication): Unit = forAllUsers {
      (username, auth) =>
        Cache.set(username, auth)
    }

    "show the index page for all roles" in new WithApplication {
      //be authenticated
      authenticateAllUsers

      forAllUsers {
        (username, auth) =>
          //make sure that cache has actual data
          Cache.get(username) must beSome(auth)

          val home = route(
            FakeRequest(GET, "/index.html").withSession(Security.username -> username)
          ).get

          status(home) must equalTo(OK)
          contentType(home) must beSome.which(_ == "text/html")
          contentAsString(home) must contain("script")
      }
    }

    "return all glossaries for all users" in new WithApplication {
      val objectMapper = SpringAwareGlobalSetting.getBean(classOf[ObjectMapper])

      Option(objectMapper) must beSome

      //be authenticated
      authenticateAllUsers

      forAllUsers {
        (username, auth) =>
          //make sure that cache has actual data
          Cache.get(username) must beSome(auth)

          val glossariesPage = route(
            FakeRequest(GET, "/glossaries").withSession(Security.username -> username)
          ).get

          status(glossariesPage) must equalTo(OK)
          contentType(glossariesPage) must beSome.which(_ == "application/json")

          val response = contentAsString(glossariesPage)

//          val page = objectMapper.readValue(response, classOf[GlossaryList]).page
//          there is no pagination
//          page.getContent must haveLength(page.getTotalElements.toInt)
      }
    }

    "return particular glossary for all users" in new WithApplication {
      val objectMapper = SpringAwareGlobalSetting.getBean(classOf[ObjectMapper])

      Option(objectMapper) must beSome

      //be authenticated
      authenticateAllUsers

      forAllUsers {
        (username, auth) =>
          //make sure that cache has actual data
          Cache.get(username) must beSome(auth)

          val id = 1

          val glossariesPage = route(
            FakeRequest(GET, s"/glossaries/$id").withSession(Security.username -> username)
          ).get

          status(glossariesPage) must equalTo(OK)
          contentType(glossariesPage) must beSome.which(_ == "application/json")

          val response = contentAsString(glossariesPage)

          val glossary = objectMapper.readValue(response, classOf[Glossary])

          glossary.id must beEqualTo(id)
      }
    }

    "logout page should redirect to login page" in new WithApplication {
      //be authenticated
      authenticateAllUsers

      forAllUsers {
        (username, auth) =>
          //make sure that cache has actual data
          Cache.get(username) must beSome(auth)

          val logoutUrl = route(
            FakeRequest(GET, "/logout").withSession(Security.username -> username)
          ).get

          status(logoutUrl) must equalTo(SEE_OTHER)
          headers(logoutUrl) must havePair(HeaderNames.LOCATION -> "/login.html")
      }
    }
  }

}

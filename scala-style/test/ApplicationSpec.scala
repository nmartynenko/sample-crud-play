import com.aimprosoft.play.glossaries.domain.GlossaryPageResponse
import com.aimprosoft.play.glossaries.models.{Glossary, User}
import com.aimprosoft.play.glossaries.security.GlossaryUserSubject
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.cache.Cache
import play.api.http.HeaderNames
import play.api.libs.json.JsObject
import play.api.mvc.Security
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification{

  "Application" should {

    "redirect from / into index.html" in new WithApplication {
      var rootRoute = route(FakeRequest(GET, "/")).get

      status(rootRoute) must equalTo(SEE_OTHER)
      headers(rootRoute) must havePair(HeaderNames.LOCATION, "/index.html")
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
    val adminUsername:String = "fakeAdmin"
    val adminSubject: GlossaryUserSubject = new GlossaryUserSubject(
      User(Some(1), "admin", "", "admin", "admin")
    )
    val userUsername: String = "fakeUser"
    val userSubject: GlossaryUserSubject = new GlossaryUserSubject(
      User(Some(2), "user", "", "user", "user")
    )
    val users: Seq[(String, GlossaryUserSubject)] = Seq(
      (adminUsername, adminSubject),
      (userUsername, userSubject)
    )

    def forAllUsers(f: => (String, GlossaryUserSubject) => Unit): Unit = {
      users foreach {
        case (username, subject) => f(username, subject)
      }
    }

    def authenticateAllUsers(implicit app: FakeApplication): Unit = forAllUsers {
      (username, subject) =>
        Cache.set(username, subject)
    }

    "show the index page for all roles" in new WithApplication {
      //be authenticated
      authenticateAllUsers

      forAllUsers {
        (username, subject) =>
          //make sure that cache has actual data
          Cache.get(username) must beSome(subject)

          val home = route(
            FakeRequest(GET, "/index.html").withSession(Security.username -> username)
          ).get

          status(home) must equalTo(OK)
          contentType(home) must beSome.which(_ == "text/html")
          contentAsString(home) must contain("script")
      }
    }

    "return all glossaries for all users" in new WithApplication {
      //be authenticated
      authenticateAllUsers

      forAllUsers {
        (username, subject) =>
          //make sure that cache has actual data
          Cache.get(username) must beSome(subject)

          val glossariesPage = route(
            FakeRequest(GET, "/glossaries").withSession(Security.username -> username)
          ).get

          status(glossariesPage) must equalTo(OK)
          contentType(glossariesPage) must beSome.which(_ == "application/json")

          val response = contentAsJson(glossariesPage).
            as[GlossaryPageResponse](controllers.GlossariesRestController.gpf)
          //there is no pagination
          response.content must haveLength(response.totalElements)
      }
    }

    "return particular glossary for all users" in new WithApplication {
      //be authenticated
      authenticateAllUsers

      forAllUsers {
        (username, subject) =>
          //make sure that cache has actual data
          Cache.get(username) must beSome(subject)

          val id = 1

          val glossariesPage = route(
            FakeRequest(GET, s"/glossaries/$id").withSession(Security.username -> username)
          ).get

          status(glossariesPage) must equalTo(OK)
          contentType(glossariesPage) must beSome.which(_ == "application/json")

          val response = contentAsJson(glossariesPage).
            as[Glossary](controllers.GlossariesRestController.gf)

          response.id must beSome.which(_ == id)
      }
    }

    "logout page should redirect to login page" in new WithApplication {
      //be authenticated
      authenticateAllUsers

      forAllUsers {
        (username, subject) =>
          //make sure that cache has actual data
          Cache.get(username) must beSome(subject)

          val logoutUrl = route(
            FakeRequest(GET, "/logout").withSession(Security.username -> username)
              //for POST/PUT requests, for others it's ignored either way
              .withJsonBody(JsObject(List()))
          ).get

          status(logoutUrl) must equalTo(SEE_OTHER)
          headers(logoutUrl) must havePair(HeaderNames.LOCATION, "/login.html")
      }
    }
  }

}

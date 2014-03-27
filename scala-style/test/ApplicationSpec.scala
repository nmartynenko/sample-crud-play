import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.libs.json.JsObject
import play.api.mvc.SimpleResult
import play.api.test._
import play.api.test.Helpers._
import scala.concurrent.Future

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  //helper methods
  def resultOfLoginPage(loginUrl: Future[SimpleResult]) {
    status(loginUrl) must equalTo(OK)
    contentType(loginUrl) must beSome.which(_ == "text/html")

    val content: String = contentAsString(loginUrl)

    Seq("form", "j_username", "j_password", "submit") foreach {
      term =>
        content must contain(term)
    }
  }

  "Application" should {

    "send 404 on a bad request" in new WithApplication{
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

      resultOfLoginPage(loginUrl)
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

      }
    }

  }

  "Application for authenticated users" should {
/*
    //be authenticated before
    doBefore {
      val loginUrl = route(
        FakeRequest(GET, "/login.html")
          //for POST/PUT requests, for others it's ignored either way
          .withJsonBody(JsObject(List()))
      ).get

      //TODO authenticate
    }
*/

    "show the index page" in new WithApplication{
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain ("Welcome")
    }.pendingUntilFixed("Should be fixed authentication")

    "get all glossaries" in new WithApplication{
      val glossariesPage = route(FakeRequest(GET, "/glossaries")).get

      status(glossariesPage) must equalTo(OK)
      contentType(glossariesPage) must beSome.which(_ == "application/json")
      contentAsString(glossariesPage) must startingWith("{")
    }.pendingUntilFixed("Should be fixed authentication")

    "access to logout page and redirect to login page" in new WithApplication{
      val logoutUrl = route(
        FakeRequest(GET, "/logout")
          //for POST/PUT requests, for others it's ignored either way
          .withJsonBody(JsObject(List()))
      ).get

      //it should be the same, as for login URL
      resultOfLoginPage(logoutUrl)
    }.pendingUntilFixed("Should be fixed authentication")
  }
}

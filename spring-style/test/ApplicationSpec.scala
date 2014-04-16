import com.aimprosoft.play.glossaries.SpringAwareGlobalSetting
import com.aimprosoft.play.glossaries.models.impl.Glossary
import com.aimprosoft.play.glossaries.security.GlossaryUserDetailsService
import com.aimprosoft.play.glossaries.service.GlossaryService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import play.api.cache.Cache
import play.api.http.{MimeTypes, HeaderNames}
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
      var rootRoute = route(
        FakeRequest(GET, "/")
      ).get

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
            FakeRequest(GET, "/index.html")
              .withSession(Security.username -> username)
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
            FakeRequest(GET, "/glossaries")
              .withSession(Security.username -> username)
          ).get

          status(glossariesPage) must equalTo(OK)
          contentType(glossariesPage) must beSome.which(_ == "application/json")

          val response = contentAsString(glossariesPage)

          val page = objectMapper.readValue(response, classOf[Map[String, _]])

          //this should be parsed well
          Option(page) must beSome

          val content: List[_] = page.get("content").get.asInstanceOf[List[_]]
          val totalElements: Int = page.get("totalElements").get.toString.toInt

          //there is no pagination
          content must haveLength(totalElements)
      }
    }

    "return particular glossary for all users" in new WithApplication {
      //spring beans
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
            FakeRequest(GET, s"/glossaries/$id")
              .withSession(Security.username -> username)
          ).get

          status(glossariesPage) must equalTo(OK)
          contentType(glossariesPage) must beSome.which(_ == "application/json")

          val response = contentAsString(glossariesPage)

          val glossary = objectMapper.readValue(response, classOf[Glossary])

          glossary.id must beEqualTo(id)
      }
    }

    "reject removal of particular glossary for regular users" in new WithApplication {
      //spring beans
      val glossaryService: GlossaryService = SpringAwareGlobalSetting.getBean(classOf[GlossaryService])
      Option(glossaryService) must beSome

      //be authenticated
      authenticateAllUsers

      //make sure that cache has actual data
      Cache.get(userUsername) must beSome(userAuth)

      val id: Long = 1

      //this glossary must be present in DB
      glossaryService.exists(id) must beTrue

      val glossariesPage = route(
        FakeRequest(DELETE, s"/glossaries/$id")
          .withSession(Security.username -> userUsername)
      ).get

      status(glossariesPage) must equalTo(UNAUTHORIZED)

      //this glossary must be still present in DB
      glossaryService.exists(id) must beTrue
    }

    "allow removal of particular glossary for admin users" in new WithApplication {
      //spring beans
      val glossaryService: GlossaryService = SpringAwareGlobalSetting.getBean(classOf[GlossaryService])
      Option(glossaryService) must beSome

      val objectMapper = SpringAwareGlobalSetting.getBean(classOf[ObjectMapper])
      Option(objectMapper) must beSome

      //be authenticated
      authenticateAllUsers

      //make sure that cache has actual data
      Cache.get(adminUsername) must beSome(adminAuth)

      val id: Long = 2

      //this glossary must be present in DB
      glossaryService.exists(id) must beTrue

      val glossariesPage = route(
        FakeRequest(DELETE, s"/glossaries/$id")
          .withSession(Security.username -> adminUsername)
      ).get

      status(glossariesPage) must equalTo(OK)

      //this glossary must be not present in DB
      glossaryService.exists(id) must beFalse
    }

    "reject adding of particular glossary for regular users" in new WithApplication {
      //spring beans
      val glossaryService: GlossaryService = SpringAwareGlobalSetting.getBean(classOf[GlossaryService])
      Option(glossaryService) must beSome

      val objectMapper = SpringAwareGlobalSetting.getBean(classOf[ObjectMapper])
      Option(objectMapper) must beSome

      //be authenticated
      authenticateAllUsers

      //make sure that cache has actual data
      Cache.get(userUsername) must beSome(userAuth)

      //initial number of glossaries
      val initialCount = glossaryService.count

      val glossary = new Glossary()
      glossary.name = "Try to add"

      val glossariesPage = route(
        FakeRequest(PUT, "/glossaries")
          .withSession(Security.username -> userUsername)
          .withBody(objectMapper.writeValueAsString(glossary))
          .withHeaders(HeaderNames.CONTENT_TYPE -> MimeTypes.JSON)
      ).get

      status(glossariesPage) must equalTo(UNAUTHORIZED)

      //number of glossaries must remain the same
      glossaryService.count must equalTo(initialCount)
    }

    "allow adding of particular glossary for admin users with no glossary's ID" in new WithApplication {
      //spring beans
      val glossaryService: GlossaryService = SpringAwareGlobalSetting.getBean(classOf[GlossaryService])
      Option(glossaryService) must beSome

      val objectMapper = SpringAwareGlobalSetting.getBean(classOf[ObjectMapper])
      Option(objectMapper) must beSome

      //be authenticated
      authenticateAllUsers

      //make sure that cache has actual data
      Cache.get(adminUsername) must beSome(adminAuth)

      //initial number of glossaries
      val initialCount = glossaryService.count

      val glossary = new Glossary()
      glossary.name = "Try to add"

      val glossariesPage = route(
        FakeRequest(PUT, "/glossaries")
          .withSession(Security.username -> adminUsername)
          .withBody(objectMapper.writeValueAsString(glossary))
          .withHeaders(HeaderNames.CONTENT_TYPE -> MimeTypes.JSON)
      ).get

      status(glossariesPage) must equalTo(OK)

      //number of glossaries must be increase by one
      glossaryService.count must equalTo(initialCount + 1)
    }

    "allow adding of particular glossary for admin users with predefined ID" in new WithApplication {
      //spring beans
      //spring beans
      val glossaryService: GlossaryService = SpringAwareGlobalSetting.getBean(classOf[GlossaryService])
      Option(glossaryService) must beSome

      val objectMapper = SpringAwareGlobalSetting.getBean(classOf[ObjectMapper])
      Option(objectMapper) must beSome

      //be authenticated
      authenticateAllUsers

      //make sure that cache has actual data
      Cache.get(adminUsername) must beSome(adminAuth)

      //initial number of glossaries
      val initialCount = glossaryService.count

      val id = 100500

      val glossary = new Glossary()
      glossary.id = id
      glossary.name = "Try to add with predefined ID"

      //this glossary must not be present in DB
      glossaryService.exists(id) must beFalse

      val glossariesPage = route(
        FakeRequest(PUT, "/glossaries")
          .withSession(Security.username -> adminUsername)
          .withBody(objectMapper.writeValueAsString(glossary))
          .withHeaders(HeaderNames.CONTENT_TYPE -> MimeTypes.JSON)
      ).get

      status(glossariesPage) must equalTo(OK)

      //this glossary must not be present in DB
      glossaryService.exists(id) must beFalse

      //number of glossaries must be increase by one
      glossaryService.count must equalTo(initialCount + 1)
    }

    "reject updating of particular glossary for regular users" in new WithApplication {
      //spring beans
      val glossaryService: GlossaryService = SpringAwareGlobalSetting.getBean(classOf[GlossaryService])
      Option(glossaryService) must beSome

      val objectMapper = SpringAwareGlobalSetting.getBean(classOf[ObjectMapper])
      Option(objectMapper) must beSome

      //be authenticated
      authenticateAllUsers

      //make sure that cache has actual data
      Cache.get(userUsername) must beSome(userAuth)

      //particular glossary's id
      val id = 1

      //this glossary must be present in DB
      glossaryService.exists(id) must beTrue

      //initial number of glossaries
      val dbGlossary = glossaryService.getById(id).get

      val newGlossary: Glossary = dbGlossary.clone().asInstanceOf[Glossary]
      newGlossary.name = "Try to update"

      //this should be different glossaries
      newGlossary mustNotEqual dbGlossary

      val glossariesPage = route(
        FakeRequest(POST, "/glossaries")
          .withSession(Security.username -> userUsername)
          .withBody(objectMapper.writeValueAsString(newGlossary))
          .withHeaders(HeaderNames.CONTENT_TYPE -> MimeTypes.JSON)
      ).get

      status(glossariesPage) must equalTo(UNAUTHORIZED)

      //stored glossary must remain the same
      glossaryService.getById(id).get must equalTo(dbGlossary)
    }

    "allow updating of particular glossary for admin users with valid id" in new WithApplication {
      //spring beans
      val glossaryService: GlossaryService = SpringAwareGlobalSetting.getBean(classOf[GlossaryService])
      Option(glossaryService) must beSome

      val objectMapper = SpringAwareGlobalSetting.getBean(classOf[ObjectMapper])
      Option(objectMapper) must beSome

      //be authenticated
      authenticateAllUsers

      //make sure that cache has actual data
      Cache.get(adminUsername) must beSome(adminAuth)

      //particular glossary's id
      val id = 3

      //this glossary must be present in DB
      glossaryService.exists(id) must beTrue

      //initial number of glossaries
      val dbGlossary = glossaryService.getById(id).get

      val newGlossary: Glossary = dbGlossary.clone().asInstanceOf[Glossary]
      newGlossary.name = "Try to update"

      //this should be different glossaries
      newGlossary mustNotEqual dbGlossary

      val glossariesPage = route(
        FakeRequest(POST, "/glossaries")
          .withSession(Security.username -> adminUsername)
          .withBody(objectMapper.writeValueAsString(newGlossary))
          .withHeaders(HeaderNames.CONTENT_TYPE -> MimeTypes.JSON)
      ).get

      status(glossariesPage) must equalTo(OK)

      //stored glossary must be updated
      glossaryService.getById(id).get must equalTo(newGlossary)
    }

    "allow updating of particular glossary for admin users with invalid id" in new WithApplication {
      //spring beans
      val glossaryService: GlossaryService = SpringAwareGlobalSetting.getBean(classOf[GlossaryService])
      Option(glossaryService) must beSome

      val objectMapper = SpringAwareGlobalSetting.getBean(classOf[ObjectMapper])
      Option(objectMapper) must beSome

      //be authenticated
      authenticateAllUsers

      //make sure that cache has actual data
      Cache.get(adminUsername) must beSome(adminAuth)

      //particular glossary's id
      val id = 100500

      //this glossary must be present in DB
      glossaryService.exists(id) must beFalse

      val dbCount = glossaryService.count

      //initial number of glossaries
      val newGlossary = new Glossary
      newGlossary.id = id
      newGlossary.name = "Try to update with predefined ID"

      val glossariesPage = route(
        FakeRequest(POST, "/glossaries")
          .withSession(Security.username -> adminUsername)
          .withBody(objectMapper.writeValueAsString(newGlossary))
          .withHeaders(HeaderNames.CONTENT_TYPE -> MimeTypes.JSON)
      ).get

      status(glossariesPage) must equalTo(OK)

      //glossary must be not saved
      glossaryService.getById(id) must beNone

      //number of elements must be increased by 1
      glossaryService.count must beEqualTo(dbCount + 1)
    }



    "logout page should redirect to login page" in new WithApplication {
      //be authenticated
      authenticateAllUsers

      forAllUsers {
        (username, auth) =>
          //make sure that cache has actual data
          Cache.get(username) must beSome(auth)

          val logoutUrl = route(
            FakeRequest(GET, "/logout")
              .withSession(Security.username -> username)
          ).get

          status(logoutUrl) must equalTo(SEE_OTHER)
          headers(logoutUrl) must havePair(HeaderNames.LOCATION -> "/login.html")
      }
    }
  }

}

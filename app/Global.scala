import play._

import org.springframework.context._
import org.springframework.context.support._

class Global extends GlobalSettings {

  private var ctx: ApplicationContext = null

  override def onStart(app: Application) {
    ctx = new ClassPathXmlApplicationContext("spring/*.xml")
  }

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    ctx.getBean(controllerClass)
  }
}
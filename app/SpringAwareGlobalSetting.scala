import java.lang.{reflect => jreflect}
import org.springframework.context._
import org.springframework.context.support._
import org.springframework.util.MethodInvoker
import org.springframework.web.bind.annotation.{ResponseBody, ResponseStatus}
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver
import org.springframework.web.method.ControllerAdviceBean
import play.api._
import play.api.mvc.Results._
import play.api.mvc.{Results, SimpleResult}
import scala.concurrent.Future

//Java2Scala conversions and vice versa
import scala.collection.JavaConversions._

object SpringAwareGlobalSetting extends GlobalSettings {

  private type Invokable = {def invoke(o: Object):  Object}

  private var ctx: ConfigurableApplicationContext = null

  private var resolvers: List[(ControllerAdviceBean, ExceptionHandlerMethodResolver)] = null

  override def onStart(app: Application) {
    Logger.info("Initialize Spring context for Play application")
    ctx = new ClassPathXmlApplicationContext("spring/*.xml")

    Logger.info("Initialization is complete")

    Logger.info("Try to find @ControllerAdvice")
    //find all @ControllerAdvice beans
    val advices = ControllerAdviceBean.findAnnotatedBeans(ctx).toList

    resolvers = advices map {advice =>
      (advice, new ExceptionHandlerMethodResolver(advice.getBeanType))
    } sortWith {
      //sort it by "order" descending
      _._1.getOrder > _._1.getOrder
    }

    Logger.info(s"Found $resolvers.size resolvers")
  }

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    ctx.getBean(controllerClass)
  }

  override def onStop(app: Application): Unit = {
    ctx.close()
  }

  override def onHandlerNotFound(request: mvc.RequestHeader): Future[SimpleResult] = {
    Future.successful(NotFound(views.html.page404()))
  }

  override def onError(request: mvc.RequestHeader, ex: Throwable): Future[SimpleResult] = {
    //helper method for getting response status with
    //checking of @ResponseHeader annotation
    def defineStatus(method: jreflect.Method) = {
      //check whether it has corresponding annotation
      method.getAnnotation(classOf[ResponseStatus]) match {
        //if it does
        case responseStatus: ResponseStatus =>
          val status = responseStatus.value()

          //return status as it is required by Annotation
          new Results.Status(status.value())
        //return InternalServerError by default
        case _ =>
          InternalServerError
      }
    }

    //helper for Method's invokation
    def invokeMethod(method: jreflect.Method, targetBean: Object, ex: Exception) = {
      method.getParameterTypes match {
        case Array() =>
          method.invoke(targetBean)
        case Array(clazz) if ex.getClass.isAssignableFrom(clazz) =>
          method.invoke(targetBean, ex)
        case _ =>
          throw new NotImplementedError("Right now there are supported either methods without parameters, or methods with single parameter, which is Exception itself")
      }
    }

    //helper method for processing Exception
    //with @ControllerAdvice beans
    def defineHandler(e: Exception): Option[Future[SimpleResult]] = {
      //try to find any resolver
      //which satisfies current Exception
      resolvers find {pair =>
        val resolvedMethod = pair._2.resolveMethod(e)
        //this method should be NOT null
        resolvedMethod != null
      } match {
        //if we found necessary
        case Some((advice, resolver)) =>
          val method = resolver.resolveMethod(e)
          val targetBean = advice.resolveBean

          //if @ResponseBody is not present, then fire a fail
          if (!method.isAnnotationPresent(classOf[ResponseBody])){
            throw new NotImplementedError("Right now only @ResponseBody handlers are supported.")
          }

          //figure out response status
          val status = defineStatus(method)

          //invoke method
          val invoke = invokeMethod(method, targetBean, e)
          invoke match {
            case content: String =>
              Some(Future.successful(status(content)))
            case _ =>
              throw new NotImplementedError("Right now only handlers with return type String are supported")
          }
        //if didn't find anything, then return None
        case None =>
          None
      }
    }

    //onError original exceptions are always wrapped with Play ones
    val handler = ex.getCause match {
      //it works only with Exceptions
      case e: Exception =>
        defineHandler(e)
      case _ =>
        None
    }

    handler.getOrElse(super.onError(request, ex))
  }

  override def onBadRequest(request: mvc.RequestHeader, error: String): Future[SimpleResult] = super.onBadRequest(request, error)
}
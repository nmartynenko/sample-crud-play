import com.fasterxml.jackson.databind.ObjectMapper
import java.lang.{reflect => jreflect}
import org.springframework.beans.factory.BeanFactoryUtils
import org.springframework.context.ApplicationContext
import org.springframework.web.bind.annotation.{ResponseBody, ResponseStatus}
import org.springframework.web.method.ControllerAdviceBean
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver
import play.api.http.ContentTypes
import play.api.mvc.Results._
import play.api.mvc.{Results, SimpleResult}
import play.api.{mvc, Logger}
import scala.Some
import scala.concurrent.Future

//Java2Scala conversions and vice versa
import scala.collection.JavaConversions._

trait ControllerAdviceProcessor {

  private var resolvers: List[(ControllerAdviceBean, ExceptionHandlerMethodResolver)] = _

  private var objectMapper: Option[ObjectMapper] = _

  def initControllerAdvice(ctx: ApplicationContext) {
    Logger.info("Try to find @ControllerAdvice")
    //find all @ControllerAdvice beans
    val advices = ControllerAdviceBean.findAnnotatedBeans(ctx).toList

    resolvers = advices map {advice =>
      (advice, new ExceptionHandlerMethodResolver(advice.getBeanType))
    } sortBy  {
      //sort it by "order" ascending
      _._1.getOrder
    }

    Logger.info(s"Found ${resolvers.size} resolvers")

    Logger.info("Try to find ObjectMapper for JSON converting")

    //find object mappers
    val beansOfType = BeanFactoryUtils.beansOfTypeIncludingAncestors(ctx, classOf[ObjectMapper])
    //find first object mapper
    objectMapper = if (beansOfType.isEmpty) None else Some(beansOfType.values().iterator().next())

    objectMapper match {
      case Some(_) => Logger.info("ObjectMapper is successfully found")
      case _ => Logger.info("ObjectMapper hasn't been found, Object support is skipped")
    }
  }

  def handleError(request: mvc.RequestHeader, ex: Throwable): Option[Future[SimpleResult]] = {
    //helper method for getting response status with
    //checking of @ResponseHeader annotation
    def defineStatus(method: jreflect.Method) = {
      //check whether it has corresponding annotation
      method.getAnnotation(classOf[ResponseStatus]) match {
        //if it does
        case responseStatus: ResponseStatus =>
          val status = responseStatus.value()

          //return status as it is required by annotation
          new Results.Status(status.value())
        //return InternalServerError by default
        case _ =>
          InternalServerError
      }
    }

    //helper for Method's invocation
    def invokeMethod(method: jreflect.Method, targetBean: Object, ex: Exception) = {
      //note: looks ugly
      method.getParameterTypes match {
        //there is no parameters
        case Array() =>
          method.invoke(targetBean)
        //if there is one parameter, which is Exception to handle
        case Array(clazz) if ex.getClass.isAssignableFrom(clazz) =>
          method.invoke(targetBean, ex)
        //if there is one parameter, which is mvc.Request
        case Array(clazz) if classOf[mvc.RequestHeader].isAssignableFrom(clazz) =>
          method.invoke(targetBean, request)
        //if there are two parameters, first one is mvc.Request and second one is Exception to handle
        case Array(first, second) if classOf[mvc.RequestHeader].isAssignableFrom(first) && ex.getClass.isAssignableFrom(second) =>
          method.invoke(targetBean, request, ex)
        //if there are two parameters, but order differs from previous case
        case Array(first, second) if classOf[mvc.RequestHeader].isAssignableFrom(second) && ex.getClass.isAssignableFrom(first) =>
          method.invoke(targetBean, ex, request)
        //otherwise fail an exception
        case _ =>
          throw new NotImplementedError("Right now there are supported either methods without parameters, or methods which " +
            "accepts mvc.Request and/or Exception itself")
      }
    }

    //helper method for processing Exception
    //with @ControllerAdvice beans
    def defineHandler(e: Exception): Option[Future[SimpleResult]] = {
      //try to find any resolver
      //which satisfies current Exception
      resolvers find {case (advice, resolver) =>
        val resolvedMethod = resolver.resolveMethod(e)
        //this method should be NOT null
        resolvedMethod != null
      } match {
        //if we found necessary pair
        case Some((advice, resolver)) => {
          val method = resolver.resolveMethod(e)
          val targetBean = advice.resolveBean

          //if returning type is not SimpleResult
          if (!classOf[SimpleResult].isAssignableFrom(method.getDeclaringClass) &&
            //if @ResponseBody is not present
            !method.isAnnotationPresent(classOf[ResponseBody])){
            //then fire a fail
            throw new NotImplementedError("Right now only @ResponseBody handlers are supported for any returning type, except SimpleResult.")
          }

          //figure out response status
          val status = defineStatus(method)

          //invoke method
          val invoke = invokeMethod(method, targetBean, e)

          val result = invoke match {
            //if returning type is Java's Void
            case obj if obj == null && method.getReturnType.equals(Void.TYPE) =>
              status
            //if it returns String
            case content: String =>
              status(content).as(ContentTypes.TEXT)
            case res: SimpleResult =>
              //ignore status for SimpleResult
              res
            //otherwise
            case obj =>
              objectMapper match {
                case Some(om) =>
                  //try to convert return instance into Plain representation
                  status(om.writeValueAsString(obj)).as(ContentTypes.JSON)
                case _ =>
                  throw new NotImplementedError(s"Could not return result for method ${method.getDeclaringClass.getName}#${method.getName}")
              }
          }

          //return Option of Future
          Some(Future.successful(result))
        }
        //if didn't find anything, then return None
        case None =>
          None
      }
    }

    //onError original exceptions are always wrapped with Play ones
    ex.getCause match {
      //it works only with Exceptions
      case e: Exception =>
        defineHandler(e)
      case _ =>
        None
    }
  }

}

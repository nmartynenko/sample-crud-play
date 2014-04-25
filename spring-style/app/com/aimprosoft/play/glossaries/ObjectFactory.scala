package com.aimprosoft.play.glossaries

import org.springframework.beans.factory.BeanFactoryUtils

trait ObjectFactory {
  def unsafeContext: PlaySpringContext
  
  def maybeContext: Option[PlaySpringContext] = Option(unsafeContext)
  
  def currentContext: PlaySpringContext = maybeContext.getOrElse(sys.error("Context hasn't been initialized"))
  
  def getBean[T](clazz: Class[T]): T = BeanFactoryUtils.beanOfTypeIncludingAncestors(currentContext, clazz)
  
  def getBean[T](beanName: String): T = currentContext.getBean(beanName).asInstanceOf[T]
}
package com.aimprosoft.scala.contrib.spring.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.springframework.beans.factory.InitializingBean
import org.springframework.util.Assert
import scala.beans.BeanProperty

class ScalaJacksonConfigurer extends InitializingBean {

  @BeanProperty
  var objectMapper: ObjectMapper = _

  def afterPropertiesSet(): Unit = {
    Assert.notNull(objectMapper, "Property [objectMapper] is not set")
    objectMapper.registerModule(DefaultScalaModule)
  }
}

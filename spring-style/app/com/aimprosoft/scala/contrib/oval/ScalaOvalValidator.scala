package com.aimprosoft.scala.contrib.oval

import java.util
import net.sf.oval.{ConstraintViolation, Validator}
import scala.beans.BeanProperty
import scala.language.reflectiveCalls

//Java2Scala conversions and vice versa
import scala.collection.JavaConversions._

class ScalaOvalValidator() {

  @BeanProperty
  var validator: Validator = new Validator()

  //using such type because of Java&Scala interoperability issues, e.g. ambiguity of methods execution
  private type Validatable = {def validate(o: Object): util.List[ConstraintViolation]}

  def validate(targetBean: AnyRef): List[ConstraintViolation] = {
    validator.asInstanceOf[Validatable].validate(targetBean).toList
  }
}

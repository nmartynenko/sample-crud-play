package com.aimprosoft.play.glossaries.models

import javax.persistence._
import scala.beans.BeanProperty

@MappedSuperclass
abstract class BusinessModel extends Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    @BeanProperty
    var id: java.lang.Long = _

}

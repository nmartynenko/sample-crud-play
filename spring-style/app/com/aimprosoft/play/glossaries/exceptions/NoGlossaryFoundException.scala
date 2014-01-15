package com.aimprosoft.play.glossaries.exceptions

import scala.beans.BeanProperty

class NoGlossaryFoundException(cause: Throwable, @BeanProperty var modelId: Long)
  extends GlossaryException(null, cause)
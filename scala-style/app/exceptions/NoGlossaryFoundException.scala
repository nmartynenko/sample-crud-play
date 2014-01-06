package exceptions

import java.lang.Long
import scala.beans.BeanProperty

class NoGlossaryFoundException(cause: Throwable, @BeanProperty var modelId: Long)
  extends GlossaryException(null, cause)
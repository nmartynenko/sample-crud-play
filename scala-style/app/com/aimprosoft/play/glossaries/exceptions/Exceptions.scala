package com.aimprosoft.play.glossaries.exceptions

abstract class ApplicationException(message: String, cause: Throwable)
  extends Exception(message, cause)

class GlossaryException(message: String = null, cause: Throwable = null)
  extends ApplicationException(message, cause)

class NoGlossaryFoundException(cause: Throwable = null, var modelId: Long)
  extends GlossaryException(null, cause)
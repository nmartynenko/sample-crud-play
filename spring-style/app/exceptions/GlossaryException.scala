package exceptions

class GlossaryException(message: String, cause: Throwable)
  extends ApplicationException(message, cause)

package exception

class GlossaryException(message: String, cause: Throwable)
  extends ApplicationException(message, cause)

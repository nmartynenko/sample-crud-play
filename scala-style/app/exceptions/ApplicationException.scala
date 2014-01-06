package exceptions

abstract class ApplicationException(message: String, cause: Throwable)
  extends Exception(message, cause)
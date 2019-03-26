package hse.nedikov.bash.exceptions

import java.lang.Exception

/**
 * Exception for incorrect arguments
 */
internal class IncorrectArgumentsException(message: String) : Exception("Incorrect arguments exception: $message")
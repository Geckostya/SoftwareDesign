package hse.nedikov.bash.exceptions

import java.lang.Exception

/**
 * Exception for parser
 */
internal class ParseException(message: String) : Exception("Parse Exception: $message")
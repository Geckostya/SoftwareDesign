package hse.nedikov.bash.exceptions

import java.lang.Exception


internal class ParseException(message: String) : Exception("Parse Exception: $message")
package hse.nedikov.bash.exceptions

import java.lang.Exception

/**
 * Exception for incorrect arguments
 */
internal class DirectoryUpdateException(message: String) : Exception("Error occurred while updating directory: $message")
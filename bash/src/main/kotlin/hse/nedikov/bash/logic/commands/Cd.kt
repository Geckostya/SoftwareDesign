package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.Environment
import hse.nedikov.bash.logic.Command
import java.io.PipedReader
import java.io.PipedWriter
import java.lang.RuntimeException

/**
 * cd command which changes the working directory
 */
class Cd(private val arguments: ArrayList<String>, private val env: Environment) : Command() {
    /**
     * Changes the working directory
     */
    override fun execute(input: PipedReader, output: PipedWriter) {
      return execute(output)
    }

    /**
     * Changes the working directory
     */
    override fun execute(output: PipedWriter) {
        if (arguments.size > 1) {
          throw RuntimeException("cd: too many arguments")
        }
        if (!env.changeDirectory(arguments.getOrElse(0) { System.getProperty("user.home") })) {
          throw RuntimeException("cd: directory not found")
        }
    }
}
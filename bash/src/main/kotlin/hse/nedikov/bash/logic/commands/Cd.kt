package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.Environment
import hse.nedikov.bash.exceptions.DirectoryUpdateException
import hse.nedikov.bash.exceptions.IncorrectArgumentsException
import hse.nedikov.bash.logic.Command
import java.io.PipedReader
import java.io.PipedWriter

/**
 * Class that switches current directory
 */
class Cd(private val arguments: ArrayList<String>, override val env: Environment) : Command(env) {
    /**
     * Changes the working directory
     */
    override fun execute(input: PipedReader, output: PipedWriter) {
        return execute(output)
    }

    /**
     * Changes the working directory
     * switches to home of zero arguments
     * switches to input dir if one argument and success
     */
    override fun execute(output: PipedWriter) {
        if (arguments.size > 1) {
            throw IncorrectArgumentsException("extra arguments in cd command")
        }

        val path = arguments.getOrElse(0) { System.getProperty("user.home") }

        env.updateDir(path)
    }
}
package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.Environment
import hse.nedikov.bash.logic.Command
import java.io.File
import java.io.PipedReader
import java.io.PipedWriter

/**
 * ls command which prints all files in current directory
 */
class Ls(private val arguments: ArrayList<String>, private val env: Environment) : Command() {
    /**
     * Prints current directory
     */
    override fun execute(input: PipedReader, output: PipedWriter) {
        return execute(output)
    }

    /**
     * Prints current directory
     */
    override fun execute(output: PipedWriter) {
        if (arguments.size > 1) {
            output.write("ls: too many arguments")
            return
        }
        printFile(env.getCanonicalFile(arguments.getOrElse(0) { "./" }), output)
    }

    private fun printFile(file: File, output: PipedWriter) {
        when {
            file.isFile -> output.write(file.name + System.lineSeparator())
            file.isDirectory -> file.listFiles().sorted().forEach { output.write(it.name + System.lineSeparator()) }
            else -> output.write("ls: file or directory not found" + System.lineSeparator())
        }
    }
}
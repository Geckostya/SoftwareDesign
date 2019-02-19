package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.Environment
import hse.nedikov.bash.logic.Command
import java.io.PipedReader
import java.io.PipedWriter

/**
 * Class that prints all files and directories in specified path or current directory
 */
class Ls(private val arguments: ArrayList<String>, override val env: Environment) : Command(env) {
    /**
     * Prints all files and directories in specified path or current directory
     * @param input input stream
     * @param output output stream
     */
    override fun execute(input: PipedReader, output: PipedWriter) {
        return execute(output)
    }

    /**
     * Prints all files and directories in specified path or current directory
     * if arguments more than one error will occur
     * @param output output stream
     */
    override fun execute(output: PipedWriter) {
        if (arguments.size > 1) {
            output.write("Error: extra args in ls command")
            return
        }

        val arg = env.getFile(arguments.getOrElse(0) { "./" })
        val sep = System.lineSeparator()

        if (!(arg.isFile || arg.isDirectory)) {
            output.write("Directory not found for ls")
            return
        }

        if (arg.isDirectory) {
            arg.listFiles().sorted().forEach {
                if (!it.isHidden) {
                    output.write(it.name + sep)
                }
            }

            return
        }

        output.write(arg.name + sep)
    }
}

package hse.nedikov.bash.logic

import hse.nedikov.bash.Environment
import hse.nedikov.bash.logic.commands.*
import hse.nedikov.bash.logic.environment.Assign
import hse.nedikov.bash.logic.environment.Exit
import java.io.*

/**
 * Base class for all interpreter commands
 */
abstract class Command {
  protected abstract fun execute(input: PipedReader, output: PipedWriter)
  protected abstract fun execute(output: PipedWriter)

  /**
   * Execute the command uses the input. As result returns new reader, which is contains the output of the command
   */
  fun execute(input: PipedReader): PipedReader {
    val reader = PipedReader()
    val writer = PipedWriter(reader)
    execute(input, writer)
    writer.close()
    return reader
  }

  /**
   * Execute the command and returns new reader, which is contains the output of the command
   */
  fun execute(): PipedReader {
    val reader = PipedReader()
    val writer = PipedWriter(reader)
    execute(writer)
    writer.close()
    return reader
  }

  companion object {
    private val regex = Regex("[A-z_\\-][A-z_\\-0-9]*=")

    /**
     * Creates a command by name, environment and list of arguments
     */
    fun create(name: String, args: ArrayList<String>, env: Environment): Command {
      if (regex.matches(name)) {
        return Assign(name.take(name.length - 1), args, env)
      }
      return when (name) {
        "echo" -> Echo(args)
        "wc" -> WordCount(args, env)
        "pwd" -> Pwd(env)
        "exit" -> Exit(env)
        "cat" -> Cat(args, env)
        "grep" -> Grep(args, env)
        "cd" -> Cd(args, env)
        else -> OuterCommand(name, args, env)
      }
    }
  }
}
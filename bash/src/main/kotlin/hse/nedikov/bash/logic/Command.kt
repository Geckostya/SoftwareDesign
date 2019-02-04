package hse.nedikov.bash.logic

import hse.nedikov.bash.Environment
import hse.nedikov.bash.logic.commands.*
import hse.nedikov.bash.logic.environment.Assign
import hse.nedikov.bash.logic.environment.Exit
import java.io.*

abstract class Command {
  protected abstract fun execute(input: PipedReader, output: PipedWriter)
  protected abstract fun execute(output: PipedWriter)

  fun execute(input: PipedReader): PipedReader {
    val reader = PipedReader()
    val writer = PipedWriter(reader)
    execute(input, writer)
    writer.close()
    return reader
  }

  fun execute(): PipedReader {
    val reader = PipedReader()
    val writer = PipedWriter(reader)
    execute(writer)
    writer.close()
    return reader
  }

  companion object {
    private val regex = Regex("[A-z_\\-][A-z_\\-0-9]*=")

    fun create(name: String, args: ArrayList<String>, env: Environment): Command {
      if (regex.matches(name)) {
        return Assign(name.take(name.length - 1), args, env)
      }
      return when (name) {
        "echo" -> Echo(args)
        "wc" -> WordCount(args)
        "pwd" -> Pwd()
        "exit" -> Exit(env)
        "cat" -> Cat(args)
        else -> OuterCommand(name, args)
      }
    }
  }
}
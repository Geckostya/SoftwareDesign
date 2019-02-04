package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.logic.Command
import java.io.*
import java.util.*

class Echo(private val arguments: ArrayList<String>) : Command() {
  override fun execute(output: PipedWriter) {
    val sj = StringJoiner(" ", "", "\n")
    arguments.forEach { sj.add(it) }
    output.write(sj.toString())
    output.close()
  }

  override fun execute(input: PipedReader, output: PipedWriter) {
    return execute(output)
  }
}
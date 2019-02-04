package hse.nedikov.bash.logic.environment

import hse.nedikov.bash.Environment
import hse.nedikov.bash.logic.EnvironmentCommand
import java.io.PipedReader
import java.io.PipedWriter
import java.lang.Exception

class Assign(private val name:String, private val arguments: ArrayList<String>, override val env: Environment)
  : EnvironmentCommand(env) {
  override fun execute(input: PipedReader, output: PipedWriter) {
  }

  override fun execute(output: PipedWriter) {
    if (arguments.isEmpty()) throw Exception("Assign exception: expected value after '='")
    env.putVariable(name, arguments[0])
  }
}
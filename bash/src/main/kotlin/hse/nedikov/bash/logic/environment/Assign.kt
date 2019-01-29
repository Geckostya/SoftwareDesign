package hse.nedikov.bash.logic.environment

import hse.nedikov.bash.Environment
import hse.nedikov.bash.logic.EnvironmentCommand
import java.io.PipedReader
import java.io.PipedWriter

class Assign(private val name:String, override val arguments: ArrayList<String>, override val env: Environment)
  : EnvironmentCommand(arguments, env) {
  override fun execute(input: PipedReader, output: PipedWriter) {
  }

  override fun execute(output: PipedWriter) {
    env.putVariable(name, arguments[0])
  }
}
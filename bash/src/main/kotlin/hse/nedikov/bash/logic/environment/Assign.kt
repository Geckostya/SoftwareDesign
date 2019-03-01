package hse.nedikov.bash.logic.environment

import hse.nedikov.bash.Environment
import hse.nedikov.bash.logic.Command
import java.io.PipedReader
import java.io.PipedWriter
import java.lang.Exception

/**
 * Class for assigning of variables
 * @param name name of variable
 */
class Assign(private val name:String, private val arguments: ArrayList<String>, override val env: Environment)
  : Command(env) {

  /**
   * Do nothing in this case
   */
  override fun execute(input: PipedReader, output: PipedWriter) {
  }

  /**
   * Assigns the first argument to the variable in the environment
   * @throws Exception if arguments list is empty
   */
  override fun execute(output: PipedWriter) {
    if (arguments.isEmpty()) throw Exception("Assign exception: expected value after '='")
    env.putVariable(name, arguments[0])
  }
}
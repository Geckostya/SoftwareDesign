package hse.nedikov.bash

import hse.nedikov.bash.Environment.State.*

/**
 * Environment of the interpreter
 */
class Environment {
  private enum class State {
    Working, Stop
  }

  private val varMap = HashMap<String, String>()
  private var state: State = Working

  /**
   * Map of the local variables
   */
  val variables: (String) -> String = { name ->
    varMap[name] ?: ""
  }

  /**
   * Put the variable with value to the variables map
   */
  fun putVariable(name: String, value: String) {
    varMap[name] = value
  }

  /**
   * Changes state to the stop value
   */
  fun stopInterpreter() {
    state = Stop
  }

  /**
   * Returns true iff interpreter is still working
   */
  fun isWorking(): Boolean = state == Working
}
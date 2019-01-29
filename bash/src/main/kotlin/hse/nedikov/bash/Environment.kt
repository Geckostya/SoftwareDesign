package hse.nedikov.bash

import hse.nedikov.bash.Environment.State.*

class Environment {
  private enum class State {
    Working, Stop
  }

  private val varMap = HashMap<String, String>()
  private var state: State = Working

  val variables: (String) -> String = { name ->
    varMap[name] ?: ""
  }

  fun putVariable(name: String, value: String) {
    varMap[name] = value
  }

  fun stopProgram() {
    state = Stop
  }

  fun isWorking(): Boolean = state == Working
}
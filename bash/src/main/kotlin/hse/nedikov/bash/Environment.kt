package hse.nedikov.bash

import hse.nedikov.bash.Environment.State.*
import java.io.File

/**
 * Environment of the interpreter
 */
class Environment {
  private enum class State {
    Working, Stop
  }

  private val varMap = HashMap<String, String>()
  private var state: State = Working
  private var curDir = File("./")

  /**
   * Switches directory
   * @param change new directory path
   * @return true is change was successful false otherwise
   */
  fun updateDir(change: String): Boolean {
    val newDirectory = getFile(change)
    val success = newDirectory.isDirectory

    if (success) {
      curDir = newDirectory
    }

    return success
  }

  /**
   * Function for full path for current path
   * @param path path to convert
   * @return full path
   */
  fun getPath(path: String): String {
    return getFile(path).canonicalPath
  }

  /**
   * Function for getting file for path
   * @param path path to file
   * @return file for path
   */
  fun getFile(path: String): File {
    return if (File(path).isAbsolute) {
      File(path).canonicalFile
    } else {
      File(curDir, path).canonicalFile
    }
  }

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
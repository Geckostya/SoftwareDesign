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
  private var workingDirectory = File("./")

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

  fun changeDirectory(newPath: String): Boolean {
    val newDirectory = getCanonicalFile(newPath)
    if (newDirectory.isDirectory) {
      workingDirectory = newDirectory
    }
    return newDirectory.isDirectory
  }

  fun getCanonicalPath(path: String): String {
    return getCanonicalFile(path).canonicalPath
  }

  fun getCanonicalFile(path: String): File {
      return if (isAbsolutePath(path)) {
        File(path)
      } else {
        File(workingDirectory, path)
      }
  }

  private fun isAbsolutePath(path: String): Boolean {
    return File.listRoots().fold(false) { res, file -> res || path.startsWith(file.canonicalPath) }
  }
}
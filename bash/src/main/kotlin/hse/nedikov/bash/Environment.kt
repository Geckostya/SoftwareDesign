package hse.nedikov.bash

import hse.nedikov.bash.Environment.State.*
import hse.nedikov.bash.exceptions.DirectoryUpdateException
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files



/**
 * Environment of the interpreter
 */
class Environment {
  private enum class State {
    Working, Stop
  }

  private val varMap = HashMap<String, String>()
  private var state: State = Working
  private var curPath = Paths.get(".").toAbsolutePath().normalize()

  /**
   * Switches directory
   * @param change new directory path
   * @return true is change was successful false otherwise
   */
  fun updateDir(change: String) {
    val newDirectory = curPath.resolve(change)
    if (!Files.exists(newDirectory) || !Files.isDirectory(newDirectory)) {
       throw DirectoryUpdateException("can't update current directory: specified directory doesn't exist or not a directory")
    }

    curPath = newDirectory.toAbsolutePath().normalize()
  }

  /**
   * Function for full path for current path
   * @param path path to convert
   * @return full path
   */
  fun getPathString(path: String): String {
    return getPath(path).toAbsolutePath().normalize().toString()
  }

  /**
   * Function for getting file for path
   * @param path path to file
   * @return file for path
   */
  private fun getPath(path: String): Path {
    return curPath.resolve(path)
  }

  /**
   * Function for getting Path for path string
   * @param path path string
   * @return Path for path string
   */
  fun getFile(path: String): File {
    return getPath(path).toFile()
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
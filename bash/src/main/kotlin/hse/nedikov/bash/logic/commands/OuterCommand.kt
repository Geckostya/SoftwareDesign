package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.logic.Command
import java.io.*
import java.util.concurrent.Executors
import java.io.InputStreamReader
import java.io.BufferedReader
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Class for calling commands in outer interpreter
 * @param name name of the command
 */
class OuterCommand(private val name: String, private val arguments: ArrayList<String>) : Command() {
  /**
   * Calls the command in outer interpreter and print theirs output or error to the output
   * in case when the command is executed in less than 10 seconds
   */
  override fun execute(input: PipedReader, output: PipedWriter) {
    val process = createProcess()

    val processWriter = OutputStreamWriter(process.outputStream)
    input.readLines().forEach { processWriter.write("$it\n") }
    processWriter.close()

    startProcess(process, output)
  }

  /**
   * Calls command in outer interpreter and print theirs output or error to the output
   * in case when the command is executed in less than 10 seconds
   */
  private fun startProcess(process: Process, output: PipedWriter) {
    process.inputStream.bufferedReader().copyTo(output)
    process.errorStream.bufferedReader().copyTo(output)
    process.waitFor()
  }

  override fun execute(output: PipedWriter) {
    val process = createProcess()
    startProcess(process, output)
  }

  private fun createProcess(): Process {
    val environmentStart = if (isWindows) "cmd.exe /c" else ""
    val command = StringJoiner(" ", "$name ", "").also { joiner -> arguments.forEach { joiner.add(it) } }.toString()
    return Runtime.getRuntime().exec("$environmentStart $command")
  }

  companion object {
    val isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows")
  }
}
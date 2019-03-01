package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.Environment
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
class OuterCommand(private val name: String, private val arguments: ArrayList<String>, override val env: Environment) : Command(env) {
  /**
   * Calls the command in outer interpreter and print theirs output or error to the output
   * in case when the command is executed in less than 10 seconds
   */
  override fun execute(input: PipedReader, output: PipedWriter) {
    val process = createProcess()

    val processWriter = OutputStreamWriter(process.outputStream)
    input.readLines().forEach { processWriter.write("$it\n") }
    processWriter.flush()

    startProcess(process, output)
  }

  /**
   * Calls command in outer interpreter and print theirs output or error to the output
   * in case when the command is executed in less than 10 seconds
   */
  private fun startProcess(process: Process, output: PipedWriter) {
    val streamGobbler = StreamGobbler(process.inputStream) { s -> output.write("$s\n") }
    val errorGobbler = StreamGobbler(process.errorStream) { s -> output.write("$s\n") }
    val executor = Executors.newSingleThreadExecutor()
    executor.submit(streamGobbler)
    executor.submit(errorGobbler)
    process.waitFor(10, TimeUnit.SECONDS)
  }

  override fun execute(output: PipedWriter) {
    val process = createProcess()
    startProcess(process, output)
  }

  private fun createProcess(): Process {
    val environmentStart = if (isWindows) "cmd.exe /c" else "sh -c"
    val command = StringJoiner(" ", "$name ", "").also { joiner -> arguments.forEach { joiner.add(it) } }.toString()
    return Runtime.getRuntime().exec("$environmentStart $command")
  }

  private class StreamGobbler(private val inputStream: InputStream, private val consumer: (String) -> Unit) : Runnable {
    override fun run() {
      BufferedReader(InputStreamReader(inputStream)).lines().forEach(consumer)
    }
  }

  companion object {
    val isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows")
  }
}
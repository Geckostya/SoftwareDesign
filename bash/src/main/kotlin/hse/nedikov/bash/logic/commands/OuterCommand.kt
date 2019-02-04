package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.logic.Command
import java.io.*
import java.util.concurrent.Executors
import java.io.InputStreamReader
import java.io.BufferedReader
import java.util.function.Consumer
import java.util.function.Supplier


class OuterCommand(val name: String, private val arguments: ArrayList<String>) : Command() {
  override fun execute(input: PipedReader, output: PipedWriter) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun execute(output: PipedWriter) {
    val process: Process
    val homeDirectory = System.getProperty("user.dir")
    if (isWindows) {
      process = Runtime.getRuntime()
          .exec(String.format("cmd.exe /c dir %s", homeDirectory))
    } else {
      process = Runtime.getRuntime()
          .exec(String.format("sh -c ls %s", homeDirectory))
    }
//    val streamGobbler = StreamGobbler(process.inputStream, System.out::println)
//    Executors.newSingleThreadExecutor().submit(streamGobbler)
    val exitCode = process.waitFor()
//    InputStreamReader(result).forEachLine { output.write("$it\n") }
  }

  private class StreamGobbler(private val inputStream: InputStream, private val consumer: Consumer<String>) : Runnable {
    override fun run() {
      BufferedReader(InputStreamReader(inputStream)).lines()
          .forEach(consumer)
    }
  }

  companion object {
    val isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows")
  }
}
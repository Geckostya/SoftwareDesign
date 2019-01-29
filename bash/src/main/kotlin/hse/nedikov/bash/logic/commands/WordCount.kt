package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.logic.Command
import java.io.*
import java.lang.Exception

class WordCount(override val arguments: ArrayList<String>) : Command(arguments) {
  override fun execute(input: PipedReader, output: PipedWriter) {
    if (arguments.isNotEmpty()) {
      return execute(output)
    }
    val r = wordCount(input)
    output.write("${r.lines} ${r.words} ${r.bytes}\n")
    output.close()
  }

  override fun execute(output: PipedWriter) {
    val result = WCResult()
    for (arg in arguments) {
      try {
        val r = wordCount(FileReader(arg))
        output.write("${r.lines} ${r.words} ${r.bytes} $arg\n")
      } catch (e: Exception) {
        output.write("wc: ${e.message}\n")
      }
    }
    output.write("${result.lines} ${result.words} ${result.bytes} total\n")
  }

  private fun wordCount(input: Reader): WCResult {
    val result = WCResult()
    input.forEachLine {
      result.lines++
      result.words += it.split(' ').size
      result.bytes += it.toByteArray().size
    }
    return result
  }

  private class WCResult {
    var lines = 0
    var words = 0
    var bytes = 0

    operator fun plusAssign(other: WCResult) {
      lines += other.lines
      words += other.words
      bytes += other.bytes
    }
  }
}
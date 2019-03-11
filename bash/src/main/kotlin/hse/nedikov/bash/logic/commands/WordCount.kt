package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.logic.Command
import java.io.*
import java.lang.Exception

/**
 * wc command which calculates count of lines, words and bytes in files or input
 */
class WordCount(private val arguments: ArrayList<String>) : Command() {
  /**
   * Calculates count of lines, words and bytes in input if arguments is empty and in files otherwise
   */
  override fun execute(input: PipedReader, output: PipedWriter) {
    if (arguments.isNotEmpty()) {
      return execute(output)
    }
    val r = calcInput(input)
    output.write("${r.lines} ${r.words} ${r.bytes}" + System.lineSeparator())
    output.close()
  }

  /**
   * Calculates count of lines, words and bytes in files and prints results for each and in total
   */
  override fun execute(output: PipedWriter) {
    val result = WCResult()
    for (arg in arguments) {
      try {
        val r = calcInput(FileReader(arg))
        output.write("${r.lines} ${r.words} ${r.bytes} $arg" + System.lineSeparator())
      } catch (e: Exception) {
        throw Exception("wc: ${e.message}" + System.lineSeparator())
      }
    }
    output.write("${result.lines} ${result.words} ${result.bytes} total" + System.lineSeparator())
  }

  private fun calcInput(input: Reader): WCResult {
    val result = WCResult()
    input.forEachLine {
      result.lines++
      result.words += it.split(Regex("\\s+")).size
      result.bytes += (it + System.lineSeparator()).toByteArray().size
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
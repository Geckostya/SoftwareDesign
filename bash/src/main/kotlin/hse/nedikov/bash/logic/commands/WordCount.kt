package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.Environment
import hse.nedikov.bash.logic.Command
import java.io.*
import java.lang.Exception

/**
 * wc command which calculates count of lines, words and bytes in files or input
 */
class WordCount(private val arguments: ArrayList<String>, override val env: Environment) : Command(env) {
  /**
   * Calculates count of lines, words and bytes in input if arguments is empty and in files otherwise
   */
  override fun execute(input: PipedReader, output: PipedWriter) {
    if (arguments.isNotEmpty()) {
      return execute(output)
    }
    val r = calcInput(input)
    output.write("${r.lines} ${r.words} ${r.bytes}\n")
    output.close()
  }

  /**
   * Calculates count of lines, words and bytes in files and prints results for each and in total
   */
  override fun execute(output: PipedWriter) {
    val result = WCResult()
    for (arg in arguments) {
      try {
        val r = calcInput(FileReader(env.getPathString(arg)))
        output.write("${r.lines} ${r.words} ${r.bytes} $arg\n")
      } catch (e: Exception) {
        output.write("wc: ${e.message}\n")
      }
    }
    output.write("${result.lines} ${result.words} ${result.bytes} total\n")
  }

  private fun calcInput(input: Reader): WCResult {
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
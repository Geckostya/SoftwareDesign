package hse.nedikov.bash.logic.commands

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.ShowHelpException
import com.xenomachina.argparser.default
import hse.nedikov.bash.logic.Command
import java.io.*
import java.lang.IllegalArgumentException
import java.util.ArrayList

/**
 * grep command which prints all lines with pattern matched substring
 */
class Grep(private val arguments: ArrayList<String>) : Command() {
  /**
   * Takes lines for grep from input
   */
  override fun execute(input: PipedReader, output: PipedWriter) {
    val args = parseArguments(::PipedGrepArgs, output) ?: return
    grep(input, output, args)
  }

  /**
   * Takes lines for grep from file from arguments
   */
  override fun execute(output: PipedWriter) {
    val args = parseArguments(::GrepArgs, output) ?: return
    grep(args.file, output, args)
  }

  private fun <T> parseArguments(constructor: (ArgParser) -> T, output: Writer): T? {
    val arr = Array(arguments.size) { i -> arguments[i] }
    try {
      return ArgParser(arr).parseInto(constructor)
    } catch (e: ShowHelpException) {
      e.printUserMessage(output, "grep", 80)
    }
    return null;
  }

  private fun grep(input: Reader, output: PipedWriter, args: PipedGrepArgs) {
    var linesToWrite = 0;
    input.forEachLine {
      val wordEnd = if (args.wordRegexp) "\\b" else ""
      val matchPattern = "$wordEnd${args.pattern}$wordEnd"
      val regex = if (args.ignoreCase) Regex(matchPattern, RegexOption.IGNORE_CASE) else Regex(matchPattern)
      if (it.contains(regex)) {
        output.write("$it\n")
        linesToWrite = args.afterContext;
      } else if (linesToWrite > 0) {
        output.write("$it\n")
        linesToWrite--
      }
    }
  }
}

private open class PipedGrepArgs(parser: ArgParser) {
  val ignoreCase by parser.flagging("-i", "--ignore-case", help = "ignore case distinctions")
  val wordRegexp by parser.flagging("-w", "--word-regexp", help = "force PATTERN to match only whole words")
  val afterContext by parser.storing("-A", "--after-context", argName = "NUM",
      help = "print NUM lines of trailing context") { toInt() }
      .default(0)
      .addValidator { if (this.value < 0) throw IllegalArgumentException("NUM should be non negative") }
  val pattern by parser.positional("PATTERN")
}

private class GrepArgs(parser: ArgParser) : PipedGrepArgs(parser) {
  val file by parser.positional("FILE") { FileReader(this) }
}
package hse.nedikov.bash

import hse.nedikov.bash.Lexer.ParserState.*
import hse.nedikov.bash.exceptions.ParseException
import java.lang.RuntimeException

/**
 * Lexer for bash interpreter
 */
class Lexer(private val data: String, private val variables: (String) -> String) {
  enum class ParserState {
    InDQuotes, InQuotes, Text, TextVariable, InDQuotesVariable, Identifier, Finished
  }

  private var state = Identifier
  private var stringBuilder = StringBuilder()
  private var varStringBuilder = StringBuilder()
  private val result = ArrayList<String>()

  /**
   * Parse input to the tokens list
   */
  fun lex(): ArrayList<String> {
    if (state == Finished) return result

    for (x in data.withIndex()) {
      val c = x.value
      parseChar(c)
    }
    if (state == InDQuotes || state == InQuotes) {
      throw ParseException("Quotes haven't closed")
    }
    if (varStringBuilder.isNotEmpty()) {
      varStringBuilder = nextVariableBuilder(varStringBuilder, stringBuilder)
    }
    if (stringBuilder.isNotEmpty()) {
      result.add(stringBuilder.toString())
    }
    state = Finished
    return result
  }

  private fun parseChar(c: Char) {
    when (state) {
      Identifier -> parseIdentifierChar(c)
      Text -> parseTextChar(c)
      InQuotes -> parseInQuotesChar(c)
      InDQuotes -> parseInDQuotesChar(c)
      TextVariable -> parseVariableChar(c, Text)
      InDQuotesVariable -> parseVariableChar(c, InDQuotes)
      Finished -> throw RuntimeException("unexpected finished state")
    }
  }

  private fun parseIdentifierChar(c: Char) {
    when (c) {
      matchIdentifier(c) -> stringBuilder.append(c)
      matchDigit(c) -> {
        if (stringBuilder.isNotEmpty()) stringBuilder.append(c)
        else {
          stringBuilder.append(c)
          state = Text
        }
      }
      '=' -> {
        stringBuilder.append(c)
        stringBuilder = nextBuilder(stringBuilder, result)
        state = Text
      }
      in whiteSpaces -> {
        if (stringBuilder.isNotEmpty()) {
          state = Text
          parseChar(c)
        }
      }
      else -> {
        state = Text
        parseChar(c)
      }
    }
  }

  private fun parseVariableChar(c: Char, parent: ParserState) {
    when (c) {
      matchIdentifier(c) -> varStringBuilder.append(c)
      matchDigit(c) -> {
        if (varStringBuilder.isNotEmpty()) varStringBuilder.append(c)
        else {
          varStringBuilder = nextVariableBuilder(varStringBuilder, stringBuilder)
          stringBuilder.append(c)
          state = Text
        }
      }
      else -> {
        varStringBuilder = nextVariableBuilder(varStringBuilder, stringBuilder)
        state = parent
        parseChar(c)
      }
    }
  }

  private fun nextVariableBuilder(sb: StringBuilder, container: StringBuilder): StringBuilder {
    container.append(variables(sb.toString()))
    return StringBuilder()
  }

  private fun parseInDQuotesChar(c: Char) {
    when (c) {
      '"' -> {
        stringBuilder = nextBuilder(stringBuilder, result)
        state = Text
      }
      '$' -> state = InDQuotesVariable
      else -> stringBuilder.append(c)
    }
  }

  private fun parseInQuotesChar(c: Char) {
    when (c) {
      '\'' -> {
        stringBuilder = nextBuilder(stringBuilder, result)
        state = Text
      }
      else -> stringBuilder.append(c)
    }
  }

  private fun parseTextChar(c: Char) {
    when (c) {
      in whiteSpaces -> {
        if (stringBuilder.isNotEmpty()) {
          stringBuilder = nextBuilder(stringBuilder, result)
        }
      }
      '"' -> state = InDQuotes
      '\'' -> state = InQuotes
      '$' -> state = TextVariable
      else -> stringBuilder.append(c)
    }
  }

  companion object {
    val whiteSpaces = Array(4) {
      when (it) {
        0 -> ' '
        1 -> '\t'
        2 -> '\n'
        else -> '\r'
      }
    }

    fun matchIdentifier(c: Char): Char = if (c in 'a'..'z' || c in 'z'..'Z' || c == '_' || c == '-') c else '_'

    fun matchDigit(c: Char): Char = if (c in '0'..'9') c else '0'

    fun nextBuilder(sb: StringBuilder, container: ArrayList<String>): StringBuilder {
      container.add(sb.toString())
      return StringBuilder()
    }
  }
}
package hse.nedikov.bash

import hse.nedikov.bash.Parser.ParserState.*

class Parser(private val data: String, private val variables: (String) -> String) {
  enum class ParserState {
    InDQuotes, InQuotes, Text, TextVariable, InDQuotesVariable, Identifier
  }

  private var state = Identifier
  private var sb = StringBuilder()
  private var vsb = StringBuilder()
  private lateinit var result: ArrayList<String>

  fun parse(): ArrayList<String> {
    result = ArrayList()
    for (x in data.withIndex()) {
      val c = x.value
      parseChar(c)
    }
    //TODO: exception with wrong state
    if (vsb.isNotEmpty()) {
      vsb = nextVariableBuilder(vsb, sb)
    }
    if (sb.isNotEmpty()) {
      result.add(sb.toString())
    }
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
    }
  }

  private fun parseIdentifierChar(c: Char) {
    when (c) {
      isIdentifier(c) -> sb.append(c)
      isDigit(c) -> {
        if (sb.isNotEmpty()) sb.append(c)
        else {
          sb.append(c)
          state = Text
        }
      }
      '=' -> {
        sb.append(c)
        sb = nextBuilder(sb, result)
        state = Text
      }
      in whiteSpaces -> {
        if (sb.isNotEmpty()) {
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
      isIdentifier(c) -> vsb.append(c)
      isDigit(c) -> {
        if (vsb.isNotEmpty()) vsb.append(c)
        else {
          vsb = nextVariableBuilder(vsb, sb)
          sb.append(c)
          state = Text
        }
      }
      else -> {
        vsb = nextVariableBuilder(vsb, sb)
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
        sb = nextBuilder(sb, result)
        state = Text
      }
      '$' -> state = InDQuotesVariable
      else -> sb.append(c)
    }
  }

  private fun parseInQuotesChar(c: Char) {
    when (c) {
      '\'' -> {
        sb = nextBuilder(sb, result)
        state = Text
      }
      else -> sb.append(c)
    }
  }

  private fun parseTextChar(c: Char) {
    when (c) {
      in whiteSpaces -> {
        if (sb.isNotEmpty()) {
          sb = nextBuilder(sb, result)
        }
      }
      '"' -> state = InDQuotes
      '\'' -> state = InQuotes
      '$' -> state = TextVariable
      else -> sb.append(c)
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

    fun isIdentifier(c: Char): Char = if (c in 'a'..'z' || c in 'z'..'Z' || c == '_' || c == '-') c else '_'

    fun isDigit(c: Char): Char = if (c in '0'..'9') c else '0'

    fun nextBuilder(sb: StringBuilder, container: ArrayList<String>): StringBuilder {
      container.add(sb.toString())
      return StringBuilder()
    }
  }
}
package hse.nedikov.bash

import hse.nedikov.bash.logic.commands.Echo
import hse.nedikov.bash.logic.commands.WordCount
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class LexerTest {
  @Test
  fun empty() {
    assertTrue(lex(ArrayList(), environment).isEmpty())
  }

  @Test
  fun simpleCommand() {
    val result = lex(list("echo"), environment)
    assertEquals(1, result.size)
    assertTrue(result[0] is Echo)
  }

  @Test
  fun commandWithArgs() {
    val result = lex(list("echo", "lol", "kek"), environment)
    assertEquals(1, result.size)
    assertTrue(result[0] is Echo)
  }

  @Test
  fun pipedCommands() {
    val result = lex(list("echo", "|", "wc"), environment)
    assertEquals(2, result.size)
    assertTrue(result[0] is Echo)
    assertTrue(result[1] is WordCount)
  }


  @Test
  fun pipedCommandsWithArg() {
    val result = lex(list("echo", "lol", "kek", "|", "wc", "cv"), environment)
    assertEquals(2, result.size)
    assertTrue(result[0] is Echo)
    assertTrue(result[1] is WordCount)
  }

  companion object {
    val environment = Environment()

    fun list(vararg values: String): ArrayList<String> = ArrayList<String>().apply { addAll(values) }
  }
}
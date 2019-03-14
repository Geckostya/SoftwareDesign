package hse.nedikov.bash

import hse.nedikov.bash.logic.commands.Echo
import hse.nedikov.bash.logic.commands.WordCount
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class ParserTest {
  @Test
  fun empty() {
    assertTrue(parse(ArrayList(), environment).isEmpty())
  }

  @Test
  fun simpleCommand() {
    val result = parse(list("echo"), environment)
    assertEquals(1, result.size)
    assertTrue(result[0] is Echo)
  }

  @Test
  fun commandWithArgs() {
    val result = parse(list("echo", "lol", "kek"), environment)
    assertEquals(1, result.size)
    assertTrue(result[0] is Echo)
  }

  @Test
  fun pipedCommands() {
    val result = parse(list("echo", "|", "wc"), environment)
    assertEquals(2, result.size)
    assertTrue(result[0] is Echo)
    assertTrue(result[1] is WordCount)
  }


  @Test
  fun pipedCommandsWithArg() {
    val result = parse(list("echo", "lol", "kek", "|", "wc", "cv"), environment)
    assertEquals(2, result.size)
    assertTrue(result[0] is Echo)
    assertTrue(result[1] is WordCount)
  }

  companion object {
    val environment = Environment()
  }
}
package hse.nedikov.bash.logic.commands

import org.junit.Test
import java.io.PipedReader
import java.io.PipedWriter
import java.util.*
import hse.nedikov.bash.list
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class CommandsTest {
  @Test
  fun echoSimple() {
    val reader = Echo(list("lol")).execute()
    assertEquals("lol", stringFromReader(reader))
  }

  @Test
  fun echoMultiArguments() {
    val reader = Echo(list("lol", "lal", "lel")).execute()
    assertEquals("lol lal lel", stringFromReader(reader))
  }

  @Test
  fun echoInputStream() {
    val reader = Echo(list("lol", "lal", "lel")).execute(readerFromString("kekes"))
    assertEquals("lol lal lel", stringFromReader(reader))
  }

  @Test
  fun catSimple() {
    val reader = Echo(list()).execute()
    assertEquals("", stringFromReader(reader))
  }

  @Test
  fun catInputStream() {
    val reader = Cat(list()).execute(readerFromString("kekes leles"))
    assertEquals("kekes leles", stringFromReader(reader))
  }

  @Test
  fun pwdSimple() {
    val reader = Pwd().execute()
    assertTrue(stringFromReader(reader).isNotEmpty())
  }

  @Test
  fun pwdSimpleWithInputStream() {
    val reader = Pwd().execute(readerFromString("kekes leles"))
    assertTrue(stringFromReader(reader).isNotEmpty())
  }

  @Test
  fun wordCountSimple() {
    val reader = WordCount(list()).execute(readerFromString("lol kek   cheburek"))
    assertEquals("1 3 " + (18 + System.lineSeparator().toByteArray().size), stringFromReader(reader))
  }

  companion object {
    fun readerFromString(string: String): PipedReader {
      val reader = PipedReader()
      PipedWriter(reader).apply { write(string) }.close()
      return reader
    }

    fun stringFromReader(reader: PipedReader): String {
      val joiner = StringJoiner(System.lineSeparator())
      reader.readLines().forEach { joiner.add(it) }
      return joiner.toString()
    }
  }
}
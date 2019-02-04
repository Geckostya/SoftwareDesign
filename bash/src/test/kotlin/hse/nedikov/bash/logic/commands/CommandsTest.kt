package hse.nedikov.bash.logic.commands

import org.junit.Test
import java.io.PipedReader
import java.io.PipedWriter
import java.util.*

class CommandsTest {

  companion object {
    fun readerFromString(string: String): PipedReader {
      val reader = PipedReader()
      PipedWriter().apply { write(string) }.close()
      return reader
    }

    fun stringFromReader(reader: PipedReader): String {
      val joiner = StringJoiner("\n")
      reader.readLines().forEach { joiner.add(it) }
      return joiner.toString()
    }
  }
}
package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.Environment
import org.junit.Test
import java.io.PipedReader
import java.io.PipedWriter
import java.util.*
import hse.nedikov.bash.list
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TemporaryFolder

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
    val reader = Cat(list(), Environment()).execute(readerFromString("kekes leles"))
    assertEquals("kekes leles", stringFromReader(reader))
  }

  @Test
  fun pwdSimple() {
    val reader = Pwd(Environment()).execute()
    assertTrue(stringFromReader(reader).isNotEmpty())
  }

  @Test
  fun pwdSimpleWithInputStream() {
    val reader = Pwd(Environment()).execute(readerFromString("kekes leles"))
    assertTrue(stringFromReader(reader).isNotEmpty())
  }

  @Test
  fun wordCountSimple() {
    val reader = WordCount(list(), Environment()).execute(readerFromString("lol kek cheburek"))
    assertEquals("1 3 16", stringFromReader(reader))
  }

  companion object {
    fun readerFromString(string: String): PipedReader {
      val reader = PipedReader()
      PipedWriter(reader).apply { write(string) }.close()
      return reader
    }

    fun stringFromReader(reader: PipedReader): String {
      val joiner = StringJoiner("\n")
      reader.readLines().forEach { joiner.add(it) }
      return joiner.toString()
    }
  }

  @Rule
  @JvmField
  val testDir = TemporaryFolder()

  @Before
  fun setup() {
    testDir.newFolder("dir")
    testDir.newFile("dir/file")
    testDir.newFile("file")
  }

  @After
  fun clear() {
    testDir.delete()
  }

  /**
   * Tests ls from current dir
   */
  @Test
  fun lsTest() {
    val env = Environment()
    env.updateDir(testDir.root.canonicalPath)
    val reader = Ls(list(), env).execute()
    assertEquals("dir\nfile", stringFromReader(reader))
  }

  /**
   * Test ls with dir in argument
   */
  @Test
  fun lsTestSubdir() {
    val env = Environment()
    env.updateDir(testDir.root.canonicalPath)
    val reader = Ls(list("dir"), env).execute()
    assertEquals("file", stringFromReader(reader))
  }

  /**
   * Test cd command forward step
   */
  @Test
  fun cdTestForward() {
    val env = Environment()
    env.updateDir(testDir.root.canonicalPath)
    Cd(list("dir"), env).execute()
    assertEquals("file", stringFromReader(Ls(list(), env).execute()))
  }

  /**
   * Test cd command backward step
   */
  @Test
  fun cdTestBackward() {
    val env = Environment()
    env.updateDir(testDir.root.canonicalPath)
    Cd(list("dir"), env).execute()
    Cd(list(".."), env).execute()
    assertEquals("dir\nfile", stringFromReader(Ls(list(), env).execute()))
  }
}
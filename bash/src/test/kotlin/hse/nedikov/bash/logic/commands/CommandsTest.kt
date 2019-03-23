package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.Environment
import org.junit.Test
import java.io.PipedReader
import java.io.PipedWriter
import java.util.*
import hse.nedikov.bash.list
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.io.File

class CommandsTest {

  @Rule
  @JvmField
  val tmpFolder = TemporaryFolder()

  @Test(expected = RuntimeException::class)
  fun lsTooManyArgumentsTest() {
    Ls(list("arg1", "arg2"), Environment()).execute()
  }

  @Test(expected = RuntimeException::class)
  fun lsDirectoryNotFoundTest() {
    Ls(list("baddirectoryyyyyy"), Environment()).execute()
  }

  @Test
  fun lsNoArgumentsTest() {
    tmpFolder.newFile("file")
    tmpFolder.newFile("oneMoreFile")
    tmpFolder.newFolder("folder")
    tmpFolder.newFolder("oneMoreFolder")
    val env = Environment()
    env.changeDirectory(tmpFolder.root.canonicalPath)
    val reader = Ls(list(), env).execute()
    assertEquals("""
      file
      folder
      oneMoreFile
      oneMoreFolder
    """.trimIndent(), stringFromReader(reader))
    tmpFolder.delete()
  }

  @Test
  fun lsOneArgumentTest() {
    tmpFolder.newFolder("folder", "oneMoreFolder")
    tmpFolder.newFile("folder/file")
    tmpFolder.newFile("folder/oneMoreFile")
    val env = Environment()
    env.changeDirectory(tmpFolder.root.canonicalPath)
    val reader = Ls(list("folder"), env).execute()
    assertEquals("""
      file
      oneMoreFile
      oneMoreFolder
    """.trimIndent(), stringFromReader(reader))
    tmpFolder.delete()
  }

  @Test
  fun lsFileTest() {
    tmpFolder.newFile("file")
    val env = Environment()
    env.changeDirectory(tmpFolder.root.canonicalPath)
    val reader = Ls(list("file"), env).execute()
    assertEquals("file", stringFromReader(reader))
    tmpFolder.delete()
  }

  @Test(expected = RuntimeException::class)
  fun cdTooManyArgumentsTest() {
    Cd(list("arg1", "arg2"), Environment()).execute()
  }

  @Test
  fun cdNoArgumentsTest() {
    val env = Environment()
    Cd(list(), env).execute()
    assertEquals(System.getProperty("user.home"), env.getCanonicalPath("./"))
  }


  @Test(expected = RuntimeException::class)
  fun cdDirectoryNotFoundTest() {
    Cd(list("baddirectoryyyyyy"), Environment()).execute()
  }

  @Test
  fun cdAndThenLsTest() {
    tmpFolder.newFolder("folder", "oneMoreFolder")
    val env = Environment()
    Cd(list(tmpFolder.root.canonicalPath), env).execute()
    val reader = Ls(list("folder"), env).execute()
    assertEquals("oneMoreFolder", stringFromReader(reader))
    tmpFolder.delete()
  }

  @Test
  fun cdRelativePathTest() {
    val env = Environment()
    Cd(list("../"), env).execute()
    assertEquals(File("../").canonicalPath, env.getCanonicalPath("./"))
  }

  @Test
  fun cdAbsoluteTest() {
    val env = Environment()
    Cd(list(File("../").canonicalPath), env).execute()
    assertEquals(File("../").canonicalPath, env.getCanonicalPath("./"))
  }

  @Test
  fun cdAndThenPwdTest() {
    val env = Environment()
    Cd(list("../"), env).execute()
    val reader = Pwd(env).execute()
    assertEquals(File("../").canonicalPath, stringFromReader(reader))
  }

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

  @Test
  fun grepTest() {
    val reader = Grep(list("lol"), Environment()).execute(readerFromString(keklolString))
    assertEquals("""
      lol kek
      kek lol
      lolol lo
      looolol
      lol
    """.trimIndent(), stringFromReader(reader))
  }

  @Test
  fun grepWordRegexpTest() {
    val reader = Grep(list("lol", "-w"), Environment()).execute(readerFromString(keklolString))
    assertEquals("""
      lol kek
      kek lol
      lol
    """.trimIndent(), stringFromReader(reader))
  }

  @Test
  fun grepIgnoreCaseTest() {
    val reader = Grep(list("LoL", "-i"), Environment()).execute(readerFromString(keklolString))
    assertEquals("""
      lol kek
      kek lol
      lolol lo
      looolol
      kekek LOL
      lol
    """.trimIndent(), stringFromReader(reader))
  }

  @Test
  fun grepAfterContextTest() {
    var reader = Grep(list("lol", "-A", "5"), Environment()).execute(readerFromString(keklolString))
    assertEquals(keklolString, stringFromReader(reader))
    reader = Grep(list("lol", "-A", "1"), Environment()).execute(readerFromString(keklolString))
    assertEquals("""
      lol kek
      kek lol
      kek kek
      lolol lo
      looolol
      kek
      lol
    """.trimIndent(), stringFromReader(reader))
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

    val keklolString = """
      lol kek
      kek lol
      kek kek
      ke
      eeek
      lolol lo
      looolol
      kek
      kekek LOL
      kekekekekek
      kekekekekekekek
      lol
    """.trimIndent()
  }
}
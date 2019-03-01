package hse.nedikov.bash

import org.junit.Assert.assertEquals
import org.junit.Test

class ParserTest {
  @Test
  fun parseEmptyString() {
    val r = Parser("") { _ -> "" }.parse()
    assertEquals(0, r.size)
  }

  @Test
  fun parseWord() {
    val r = Parser("lol") { _ -> "" }.parse()
    assertEquals(1, r.size)
    assertEquals("lol", r[0])
  }

  @Test
  fun parseTwoWords() {
    val r = Parser("lol lal") { "" }.parse()
    assertEquals(2, r.size)
    assertEquals("lol", r[0])
    assertEquals("lal", r[1])
  }

  @Test
  fun parseVariable() {
    val r = Parser("\$lol", lolToLal).parse()
    assertEquals(1, r.size)
    assertEquals("lal", r[0])
  }

  @Test
  fun parseVariable2() {
    val r = Parser("\$lol \$lel", lolToLal).parse()
    assertEquals(1, r.size)
    assertEquals("lal", r[0])
  }

  @Test
  fun parseInQuotes() {
    val r = Parser("'lal lol '") { "" }.parse()
    assertEquals(1, r.size)
    assertEquals("lal lol ", r[0])
  }

  @Test
  fun parseInDQuotes() {
    val r = Parser("\"lal lol \"") { "" }.parse()
    assertEquals(1, r.size)
    assertEquals("lal lol ", r[0])
  }

  @Test
  fun parseInDQuotesWithVariable() {
    val r = Parser("\"lal \$lol \"", lolToLal).parse()
    assertEquals(1, r.size)
    assertEquals("lal lal ", r[0])
  }

  @Test
  fun parseInQuotesWithVariable() {
    val r = Parser("'lal \$lol '", lolToLal).parse()
    assertEquals(1, r.size)
    assertEquals("lal \$lol ", r[0])
  }

  @Test
  fun parseAssign() {
    val r = Parser("  lol=kek", lolToLal).parse()
    assertEquals(2, r.size)
    assertEquals("lol=", r[0])
    assertEquals("kek", r[1])
  }

  @Test
  fun parseAssignInQuotes() {
    val r = Parser("  lol='kek lol'", lolToLal).parse()
    assertEquals(2, r.size)
    assertEquals("lol=", r[0])
    assertEquals("kek lol", r[1])
  }

  companion object {
    val lolToLal: (String) -> String = {
      when (it) {
        "lol" -> "lal"
        else -> ""
      }
    }
  }
}
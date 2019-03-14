package hse.nedikov.bash

import org.junit.Assert.assertEquals
import org.junit.Test

class LexerTest {
  @Test
  fun lexEmptyString() {
    val r = Lexer("") { _ -> "" }.lex()
    assertEquals(0, r.size)
  }

  @Test
  fun lexWord() {
    val r = Lexer("lol") { _ -> "" }.lex()
    assertEquals(1, r.size)
    assertEquals("lol", r[0])
  }

  @Test
  fun lexTwoWords() {
    val r = Lexer("lol lal") { "" }.lex()
    assertEquals(2, r.size)
    assertEquals("lol", r[0])
    assertEquals("lal", r[1])
  }

  @Test
  fun lexVariable() {
    val r = Lexer("\$lol", lolToLal).lex()
    assertEquals(1, r.size)
    assertEquals("lal", r[0])
  }

  @Test
  fun lexVariable2() {
    val r = Lexer("\$lol \$lel", lolToLal).lex()
    assertEquals(1, r.size)
    assertEquals("lal", r[0])
  }

  @Test
  fun lexInQuotes() {
    val r = Lexer("'lal lol '") { "" }.lex()
    assertEquals(1, r.size)
    assertEquals("lal lol ", r[0])
  }

  @Test
  fun lexInDQuotes() {
    val r = Lexer("\"lal lol \"") { "" }.lex()
    assertEquals(1, r.size)
    assertEquals("lal lol ", r[0])
  }

  @Test
  fun lexInDQuotesWithVariable() {
    val r = Lexer("\"lal \$lol \"", lolToLal).lex()
    assertEquals(1, r.size)
    assertEquals("lal lal ", r[0])
  }

  @Test
  fun lexInQuotesWithVariable() {
    val r = Lexer("'lal \$lol '", lolToLal).lex()
    assertEquals(1, r.size)
    assertEquals("lal \$lol ", r[0])
  }

  @Test
  fun lexAssign() {
    val r = Lexer("  lol=kek", lolToLal).lex()
    assertEquals(2, r.size)
    assertEquals("lol=", r[0])
    assertEquals("kek", r[1])
  }

  @Test
  fun lexAssignInQuotes() {
    val r = Lexer("  lol='kek lol'", lolToLal).lex()
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
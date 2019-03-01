package hse.nedikov.bash.logic.environment

import hse.nedikov.bash.Environment
import hse.nedikov.bash.list
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class EnvironmentCommandsTest {
  lateinit var environment: Environment
  @Before
  fun createEnvironment() {
    environment = Environment()
    environment.putVariable("x", "lel")
  }

  @Test
  fun testAssign() {
    assertEquals("lel", environment.variables("x"))
    Assign("x", list("lal"), environment).execute()
    assertEquals("lal", environment.variables("x"))
  }

  @Test
  fun testExit() {
    assertTrue(environment.isWorking())
    Exit(environment).execute()
    assertFalse(environment.isWorking())
  }
}
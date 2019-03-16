package hse.nedikov.bash

import hse.nedikov.bash.logic.Command
import java.io.PipedReader
import java.io.PipedWriter


class Controller(val exceptionHandler: (Exception) -> Unit) {
  private val env = Environment()

  fun isWorking() = env.isWorking()

  fun runCommandLine(line: String, outputHandler: (String) -> Unit) {
    val tokens = Lexer(line, env.variables).lex()
    val commands = parse(tokens, env)
    val flow = makeFlow(commands);
    flow.invoke().forEachLine { outputHandler(it) }
  }

  fun makeFlow(commands: ArrayList<Command>): () -> PipedReader =
      if (commands.isEmpty()) {
        { emptyCommandFlow() }
      } else commandFlow(commands[0], commands.apply { removeAt(0) })

  private fun commandFlow(command: Command, commands: ArrayList<Command>): () -> PipedReader = {
    commands.reversed().fold(command.execute()) { reader, command ->
      try {
        command.execute(reader)
      } catch (e: Exception) {
        exceptionHandler(e)
        emptyCommandFlow()
      }
    }
  }

  private fun emptyCommandFlow(): PipedReader {
    val reader = PipedReader()
    PipedWriter(reader).close()
    return reader
  }
}

package hse.nedikov.bash

import hse.nedikov.bash.logic.Command
import java.io.PipedReader
import java.io.PipedWriter

fun main(args: Array<String>) {
  val env = Environment()
  while (env.isWorking()) {
    try {
      val line = readLine()
      val commands = lex(Parser(line ?: "", env.variables).parse(), env)
      val flow = makeFlow(commands);
      flow.invoke().forEachLine { println(it) }
    } catch (e: Exception) {
      println("error: ${e.message}")
    }
  }
}

private fun makeFlow(commands: ArrayList<Command>) =
    if (commands.isEmpty()) emptyCommandFlow()
    else commandFlow(commands[0], commands.apply { removeAt(0) })

private fun commandFlow(command: Command, commands: ArrayList<Command>): () -> PipedReader = {
  commands.reversed().fold(command.execute()) { reader, command ->
    command.execute(reader)
  }
}


private fun emptyCommandFlow(): () -> PipedReader = {
  val reader = PipedReader()
  PipedWriter(reader).close()
  reader
}

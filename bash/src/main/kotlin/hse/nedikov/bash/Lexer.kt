package hse.nedikov.bash

import hse.nedikov.bash.logic.Command
import java.io.*

fun lex(tokens: ArrayList<String>, env: Environment): () -> PipedReader {
  if (tokens.isEmpty()) return emptyCommandFlow()
  var res = lexCommand(tokens, 0, env)
  var i = res.first
  val command = res.second
  val commands = ArrayList<Command>()
  while (i < tokens.size) {
    res = lexCommand(tokens, i, env)
    i = res.first
    commands.add(res.second)
  }
  return commandFlow(command, commands)

}

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

private fun lexCommand(tokens: ArrayList<String>, start: Int, env: Environment): Pair<Int, Command> {
  var i = start
  val name = tokens[i++]
  val args = ArrayList<String>()
  while (i < tokens.size && tokens[i] != "|") {
    args.add(tokens[i++])
  }
  return (i + 1) to Command.create(name, args, env)
}


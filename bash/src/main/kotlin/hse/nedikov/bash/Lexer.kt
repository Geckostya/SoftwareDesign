package hse.nedikov.bash

import hse.nedikov.bash.logic.Command
import java.io.*

fun lex(tokens: ArrayList<String>, env: Environment): ArrayList<Command> {
  val commands = ArrayList<Command>()
  var i = 0;
  while (i < tokens.size) {
    val res = lexCommand(tokens, i, env)
    i = res.first
    commands.add(res.second)
  }
  return commands
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


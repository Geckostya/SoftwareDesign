package hse.nedikov.bash

import hse.nedikov.bash.logic.Command

private const val PIPE = "|"

/**
 * Creates a list of commands by string tokens in pipes
 */
fun parse(tokens: ArrayList<String>, env: Environment): ArrayList<Command> {
  if (tokens.isNotEmpty() && tokens.last() == PIPE) {
    throw Exception("$PIPE in end of command line")
  }
  val commands = ArrayList<Command>()
  var i = 0;
  while (i < tokens.size) {
    val res = parseCommand(tokens, i, env)
    i = res.first
    commands.add(res.second)
  }
  return commands
}

private fun parseCommand(tokens: ArrayList<String>, start: Int, env: Environment): Pair<Int, Command> {
  var i = start
  val name = tokens[i++]
  val args = ArrayList<String>()
  while (i < tokens.size && tokens[i] != PIPE) {
    args.add(tokens[i++])
  }
  return (i + 1) to Command.create(name, args, env)
}


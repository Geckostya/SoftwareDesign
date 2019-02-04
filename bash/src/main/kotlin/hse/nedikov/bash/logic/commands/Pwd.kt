package hse.nedikov.bash.logic.commands

import hse.nedikov.bash.logic.Command
import java.io.*


class Pwd : Command() {
  override fun execute(input: PipedReader, output: PipedWriter) {
    return execute(output)
  }

  override fun execute(output: PipedWriter) {
    output.write(System.getProperty("user.dir") + "\n")
  }

}
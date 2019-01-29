package hse.nedikov.bash.logic

import hse.nedikov.bash.Environment

abstract class EnvironmentCommand(
    override val arguments: ArrayList<String>,
    open val env: Environment
) : Command(arguments)
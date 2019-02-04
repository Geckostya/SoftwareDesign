package hse.nedikov.bash.logic

import hse.nedikov.bash.Environment

abstract class EnvironmentCommand(open val env: Environment) : Command()
package hse.nedikov.bash.logic

import hse.nedikov.bash.Environment

/**
 * Base class for commands which uses environment
 */
abstract class EnvironmentCommand(open val env: Environment) : Command()
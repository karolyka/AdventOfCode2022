import java.io.File

const val INPUT_FILE_NAME = "day10/input.txt"
//const val INPUT_FILE_NAME = "day10/input_test.txt"

private const val CMD_ADDX = "addx"
private const val CMD_NOOP = "noop"
private const val CMD_DELIMITER = ' '
private const val CYCLES_ADDX = 2
private const val CYCLES_NOOP = 1

private const val OFFSET = 20
private const val WIDE = 40

private const val EMPTY = ' '
private const val DOT = '#'
private const val MAX_CRT_LINES = 7

private val spriteRange = (0..2)

fun main() {
    val lines = File(INPUT_FILE_NAME).readLines().toTypedArray()

    var steps = 0
    var registerX = CYCLES_NOOP
    var sum = 0
    val crt = (0 until MAX_CRT_LINES).map { (0 until WIDE).map { EMPTY }.toCharArray() }.toTypedArray()

    lines.map { it.toCommand() }.forEach { command ->
        while (command.isNotDone()) {
            steps++

            val windowPosition = (steps - CYCLES_NOOP) % WIDE
            val c = if ((CYCLES_NOOP + windowPosition - registerX) in spriteRange) DOT else EMPTY
            crt[steps / WIDE][windowPosition] = c

            if ((steps + OFFSET) % WIDE == 0) {
                sum += steps * registerX
            }
            registerX = command.doCommand(registerX)

        }
    }

    println("Task1")
    println(sum)

    println("\nTask2")
    crt.map { it.joinToString("") }.forEach { println(it) }
}

private data class Command(val name: String, val cycles: Int, val value: Int? = null) {
    var cycle = 0

    fun isDone() = !isNotDone()
    fun isNotDone() = cycle < cycles
    fun doCommand(parameter: Int): Int {
        var result = parameter
        if (isNotDone()) {
            cycle++

            if (isDone()) {
                result = when (name) {
                    CMD_NOOP -> parameter
                    CMD_ADDX -> parameter + value!!
                    else -> throw IllegalArgumentException("Unsupported command")
                }
            }
        }
        return result
    }

}

private fun String.toCommand(): Command {
    return when (val name = substringBefore(CMD_DELIMITER)) {
        CMD_NOOP -> Command(name, cycles = CYCLES_NOOP)
        CMD_ADDX -> Command(name, cycles = CYCLES_ADDX, substringAfter(CMD_DELIMITER).toInt())
        else -> throw IllegalArgumentException("Unsupported command")
    }
}

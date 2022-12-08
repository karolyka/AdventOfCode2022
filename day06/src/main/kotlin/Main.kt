import java.io.File

const val INPUT_FILE_NAME = "day06/input.txt"
//const val INPUT_FILE_NAME = "day06/input_test.txt"

private const val CHAR_NUM_TASK1 = 4
private const val CHAR_NUM_TASK2 = 14

fun main(args: Array<String>) {
    val lines = File(INPUT_FILE_NAME).readLines()

    listOf(CHAR_NUM_TASK1, CHAR_NUM_TASK2)
        .forEachIndexed { index, charNum ->
            println("Task $index")
            lines.forEach { line ->
                println(line.windowed(charNum).indexOfFirst { it.toSet().size == charNum } + charNum)
            }
        }
}
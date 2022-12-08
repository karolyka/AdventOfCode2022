import java.io.File

//const val INPUT_FILE_NAME = "day06/input.txt"
const val INPUT_FILE_NAME = "day06/input_test.txt"

private const val CHAR_NUM = 4
private const val CHAR_NUM2 = 14

fun main(args: Array<String>) {
    val lines = File(INPUT_FILE_NAME).readLines()

    println("Task 1")

    lines.forEach { line ->
        println(line.windowed(CHAR_NUM).indexOfFirst { it.toSet().size == CHAR_NUM } + CHAR_NUM)
    }

    println("Task 2")

    lines.forEach { line ->
        println(line.windowed(CHAR_NUM2).indexOfFirst { it.toSet().size == CHAR_NUM2 } + CHAR_NUM2)
    }
}
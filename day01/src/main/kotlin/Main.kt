import java.io.File

const val INPUT_FILE_NAME = "day01/input.txt"

//const val INPUT_FILE_NAME = "day01/input_test.txt"
const val MOST_ELF_COUNT = 3

fun main(args: Array<String>) {
    val lines = File(INPUT_FILE_NAME).readLines()

    val elfCalories = mutableMapOf<Int, Int>()
    var index = 1

    lines.forEach { line ->
        if (line.isNotBlank()) {
            elfCalories[index] = (elfCalories[index] ?: 0) + line.toInt()
        } else {
            index++
        }
    }

    val elves = elfCalories.map { it }.sortedByDescending { it.value }.subList(0, MOST_ELF_COUNT).toList()

    println("The most $MOST_ELF_COUNT Elves ${elves.sumOf { it.value }} - $elves")
}

import java.io.File

const val INPUT_FILE_NAME = "day08/input.txt"
//const val INPUT_FILE_NAME = "day08/input_test.txt"

fun main(args: Array<String>) {
    val lines = File(INPUT_FILE_NAME).readLines().toTypedArray()
    val sizeWidth = lines.first().length
    val sizeHeight = lines.size

    val columns = (0 until sizeWidth).map { index -> lines.map { it[index] }.joinToString("") }.toTypedArray()

    val counter = (1 until sizeHeight - 1).sumOf { rowIndex ->
        val currentLine = lines[rowIndex]
        (1 until sizeWidth - 1).map { columnIndex ->
            val currentColumn = columns[columnIndex]
            val current = currentLine[columnIndex]

            currentLine.subSequence(0, columnIndex).all { it < current } ||
                    currentLine.subSequence(columnIndex + 1, sizeWidth).all { it < current } ||
                    currentColumn.subSequence(0, rowIndex).all { it < current } ||
                    currentColumn.subSequence(rowIndex + 1, sizeHeight).all { it < current }
        }.count { it }
    }

    println("Task1")
    val edgeTrees = 2 * (sizeWidth + sizeHeight) - 4
    println("$counter + $edgeTrees = ${counter + edgeTrees}")

    val counter2 = (1 until sizeHeight - 1).maxOf { rowIndex ->
        val currentLine = lines[rowIndex]
        (1 until sizeWidth - 1).maxOf { columnIndex ->
            val currentColumn = columns[columnIndex]
            val current = currentLine[columnIndex]

            getDistance(current, currentLine.subSequence(0, columnIndex), true) *
                    getDistance(current, currentLine.subSequence(columnIndex + 1, sizeWidth)) *
                    getDistance(current, currentColumn.subSequence(0, rowIndex), true) *
                    getDistance(current, currentColumn.subSequence(rowIndex + 1, sizeHeight))
        }
    }
    println("\nTask2")
    println("$counter2")
}

fun getDistance(current: Char, sequence: CharSequence, back: Boolean = false): Int {
    val view = if (back) sequence.reversed() else sequence
    val result = view.indexOfFirst { it >= current } + 1

    return if (result == 0) sequence.length else result
}

import java.io.File

const val INPUT_FILE_NAME = "day04/input.txt"
//const val INPUT_FILE_NAME = "day04/input_test.txt"

fun main() {

    val lines = File(INPUT_FILE_NAME).readLines()

    val rangePairs = lines.map { it.toRangePair() }

    val coveredCount = rangePairs.count { it.isCovered() }
    println("Fully covered pairs: $coveredCount")

    val overlappedCount = rangePairs.count { it.isOverlapped() }
    println("Overlapped pairs: $overlappedCount")
}

private fun String.toRangePair() = this.split(',').map { group ->
    group.split('-').map { it.toInt() }.let { (it[0]..it[1]) }
}.let { it[0] to it[1] }

private fun Pair<IntRange, IntRange>.isCovered() =
    (first.first <= second.first && first.last >= second.last) ||
            (second.first <= first.first && second.last >= first.last)

private fun Pair<IntRange, IntRange>.isOverlapped() =
    !(second.last < first.first || second.first > first.last)

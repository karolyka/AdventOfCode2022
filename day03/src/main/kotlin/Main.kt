import java.io.File

const val INPUT_FILE_NAME = "day03/input.txt"
//const val INPUT_FILE_NAME = "day03/input_test.txt"

fun main(args: Array<String>) {

    val lines = File(INPUT_FILE_NAME).readLines()

    val pairs = lines.sumOf { line ->
        line.length.div(2).let {
            (line.substring(0, it).toSet() intersect line.substring(it).toSet()).toCode()
        }
    }

    println(pairs)

    val lineSet = lines.map { it.toSet() }
    val sum = lineSet.chunked(3).sumOf { it.getCommon().toCode() }

    println(sum)
}

private fun Set<Char>.toCode() = if (size != 1) throw IllegalArgumentException(toString()) else
    this.first().let { c ->
        when (c) {
            in 'a'..'z' -> c.code - 96
            in 'A'..'Z' -> c.code - 38
            else -> throw IllegalArgumentException(toString())
        }
    }

private fun List<Set<Char>>.getCommon(): Set<Char> {
    var firstGroupsCommon = first()
    forEach { firstGroupsCommon = firstGroupsCommon intersect it }
    if (firstGroupsCommon.size != 1)
        throw IllegalArgumentException(firstGroupsCommon.toString())
    return firstGroupsCommon
}
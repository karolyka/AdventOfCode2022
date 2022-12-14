import java.io.File

const val INPUT_FILE_NAME = "day12/input.txt"
//const val INPUT_FILE_NAME = "day12/input_test.txt"

fun main() {
    val lines = File(INPUT_FILE_NAME).readLines().toTypedArray()
    val heightMap = parseLines(lines)
    val start = heightMap.firstNotNullOf { if (it.value.height < 0) it else null }.also { it.value.step = 0 }
    val end = heightMap.firstNotNullOf { if (it.value.height > 'z' - 'a') it else null }

    heightMap.keys.forEach { position ->
        position.neighbors = setOf(
            Position(position.x - 1, position.y),
            Position(position.x + 1, position.y),
            Position(position.x, position.y - 1),
            Position(position.x, position.y + 1)
        ).filter { heightMap.keys.contains(it) }.toSet()
    }

    val task1 = findPath(heightMap, { it == end.value }, { from, to -> to.height - from.height <= 1 })

    println("Task1")
    println("${task1.step}")

    heightMap.values.forEach { it.step = Int.MAX_VALUE }
    end.value.step = 0
    val task2 = findPath(heightMap, { it.height == 0 }, { from, to -> from.height - to.height <= 1 })

    println("\nTask2")
    println("${task2.step}")
}

private fun findPath(
    heightMap: Map<Position, PositionValue>,
    isDone: (PositionValue) -> Boolean,
    canStep: (PositionValue, PositionValue) -> Boolean
): PositionValue {
    var step = 0
    while (step <= heightMap.size) {
        step++
        heightMap
            .filter { it.value.step < Int.MAX_VALUE }
            .forEach { position ->
                position.key.neighbors
                    .map { heightMap[it]!! }
                    .filter { it.step > position.value.step }
                    .filter { canStep(position.value, it) }
                    .forEach {
                        it.step = step
                        if (isDone(it)) return it
                    }
            }
    }
    throw IllegalStateException("There is no path :(")
}

private data class Position(val x: Int, val y: Int) {
    lateinit var neighbors: Set<Position>
}

private data class PositionValue(val height: Int, var step: Int = Int.MAX_VALUE)

private fun parseLines(lines: Array<String>) = lines.flatMapIndexed { column: Int, line: String ->
    line.mapIndexed { row, character ->
        Position(row, column) to PositionValue(
            when (character) {
                in 'a'..'z' -> character - 'a'
                'S' -> -1
                'E' -> 'z' - 'a' + 1
                else -> throw IllegalArgumentException("Unsupported character: [$character]")
            }
        )
    }
}.toMap()
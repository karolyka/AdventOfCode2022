import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.sign

const val INPUT_FILE_NAME = "day09/input.txt"
//const val INPUT_FILE_NAME = "day09/input_test.txt"
//const val INPUT_FILE_NAME = "day09/input_test02.txt"

private const val SHORT_KNOTS = 2
private const val LONG_KNOTS = 10

fun main() {
    val lines = File(INPUT_FILE_NAME).readLines().toTypedArray()

    val rope = Rope(SHORT_KNOTS)
    lines.map { it.toStep() }.forEach { step ->
        repeat(step.steps) { rope.move(step.direction) }
    }

    println("Task1")
    println(rope.positions.size)

    val longRope = Rope(LONG_KNOTS)
    lines.map { it.toStep() }.forEach { step ->
        repeat(step.steps) { longRope.move(step.direction) }
    }

    println("\nTask2")
    println(longRope.positions.size)
}

private enum class DIRECTION(val code: Char) {
    UP('U'),
    DOWN('D'),
    LEFT('L'),
    RIGHT('R')
}

private data class Step(val direction: DIRECTION, val steps: Int)

private fun String.toStep() = Step(DIRECTION.values().first { it.code == first() }, substringAfter(' ').toInt())

private data class Position(var x: Int, var y: Int) {
    fun maxDistance() = maxOf(x.absoluteValue, y.absoluteValue)
    operator fun minus(other: Position) = Position(this.x - other.x, y - other.y)
    operator fun compareTo(i: Int) = maxDistance().compareTo(i)

    operator fun plusAssign(delta: Position) {
        x += delta.x.sign
        y += delta.y.sign
    }
}

private data class Rope(
    val length: Int,
    val knots: Array<Position> = (0 until length).map { Position(0, 0) }.toTypedArray()
) {
    val positions = mutableSetOf(knots.last().copy())

    fun move(direction: DIRECTION) {
        when (direction) {
            DIRECTION.UP -> up()
            DIRECTION.DOWN -> down()
            DIRECTION.LEFT -> left()
            DIRECTION.RIGHT -> right()
        }
        positions.add(knots.last().copy())
    }

    private fun up() {
        knots.first().y++
        tails()
    }

    private fun down() {
        knots.first().y--
        tails()
    }

    private fun right() {
        knots.first().x++
        tails()
    }

    private fun left() {
        knots.first().x--
        tails()
    }

    private fun tails() {
        var head = knots.first()
        var it = 1
        while (it < length) {
            val current = knots[it]
            val delta = head - current
            if (delta.maxDistance() < SHORT_KNOTS) {
                return
            }
            current += delta
            head = current
            it++
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rope

        if (!knots.contentEquals(other.knots)) return false

        return true
    }

    override fun hashCode(): Int {
        return knots.contentHashCode()
    }
}
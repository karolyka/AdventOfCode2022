import java.io.File

const val INPUT_FILE_NAME = "day02/input.txt"
//const val INPUT_FILE_NAME = "day02/input_test.txt"

private const val CARDS = 3
private const val POINT_LOSE = 0
private const val POINT_DRAW = 3
private const val POINT_WIN = 6

fun main() {

    val cardMap = buildCardMap()

    val lines = File(INPUT_FILE_NAME).readLines()

    val rounds = lines.map { it.toRound() }.groupBy { it }.mapValues { it.value.size }
    playAndWriteResult(rounds, cardMap)

    val secondRounds = mapToSecondRound(rounds)
    playAndWriteResult(secondRounds, cardMap)
}

private fun playAndWriteResult(
    rounds: Map<Round, Int>,
    cardMap: Map<CARD, Map<CARD, Int>>
) {
    val points = rounds.toPoints(cardMap)

    println(rounds)
    println(points)
    println(points.getSum())
}

private fun mapToSecondRound(rounds: Map<Round, Int>) = rounds.map { round ->
    Round(round.key.a,
        when (round.key.b) {
            CARD.A -> CARD.values().first { round.key.a.ordinal == it.ordinal.inc().mod(CARDS) }
            CARD.B -> round.key.a
            CARD.C -> CARD.values().first { it.ordinal == round.key.a.ordinal.inc().mod(CARDS) }
        }) to round.value
}.toMap()

private fun buildCardMap() = CARD.values().associateWith { left ->
    CARD.values().associateWith { right ->
        when (left.ordinal) {
            right.ordinal -> POINT_DRAW
            right.ordinal.inc().mod(CARDS) -> POINT_LOSE
            else -> POINT_WIN
        } + right.point
    }
}

private fun Map<Round, Int>.toPoints(cardMap: Map<CARD, Map<CARD, Int>>) = map { it to cardMap[it.key.a]!![it.key.b]!! }

private fun List<Pair<Map.Entry<Round, Int>, Int>>.getSum() = sumOf { it.first.value * it.second }

private fun Char.toCard() = CARD.values().firstOrNull { it.alias == this } ?: CARD.valueOf(this.toString())
private fun String.toRound() = Round(this[0].toCard(), this[2].toCard())

private data class Round(val a: CARD, val b: CARD)

private enum class CARD(val cardName: String, val alias: Char, val point: Int) {
    A("Rock", 'X', 1),
    B("Paper", 'Y', 2),
    C("Scissors", 'Z', 3);
}

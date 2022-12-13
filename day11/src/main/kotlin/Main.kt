import java.io.File

const val INPUT_FILE_NAME = "day11/input.txt"
//const val INPUT_FILE_NAME = "day11/input_test.txt"

fun main() {
    val lines = File(INPUT_FILE_NAME).readLines().toTypedArray()

    var monkeys = getMonkeys(lines, 20) { it / 3 }

    println("Task1")
    printResult(monkeys)

    val common = monkeys.map { it.divisibleBy.toLong() }.reduce(Long::times)
    monkeys = getMonkeys(lines, 10_000) { it % common }
    println("\nTask2")
    printResult(monkeys)
}

private fun printResult(monkeys: MutableList<Monkey>) {
    monkeys.map { it.inspectCount }.sortedDescending().take(2).let {
        println("$it = ${it[0] * it[1]}")
    }
}

private fun getMonkeys(lines: Array<String>, iterateCount: Int, operation: (Long) -> Long): MutableList<Monkey> {
    val monkeys = parseMonkeys(lines)

    repeat(iterateCount) {
        monkeys.forEach { monkey ->
            monkey.worryLevels.forEach {
                val worryLevel = operation(monkey.operation.process(it))
                monkeys[monkey.getTarget(worryLevel)].worryLevels.add(worryLevel)
            }
            monkey.worryLevels.clear()
        }
    }
    return monkeys
}

private fun parseMonkeys(lines: Array<String>): MutableList<Monkey> {
    val monkeys = mutableListOf<Monkey>()
    var monkey = Monkey(-1)
    lines.map { it.trim() }.forEach { line ->
        when {
            line.isBlank() -> {}
            line.startsWith("Monkey ") -> monkey =
                Monkey(line.substringAfter(' ').trim(':').toInt()).also { monkeys.add(it) }

            line.startsWith("Starting items:") -> monkey.worryLevels = line.substringAfter(':').split(',')
                .map { s -> s.trimThanToInt().toLong() }
                .toMutableList()

            line.startsWith("Operation:") -> monkey.operation = Operation(line.substringAfter(':'))
            line.startsWith("Test:") -> monkey.divisibleBy = line.substringAfter("by").trimThanToInt()
            line.startsWith("If true:") -> monkey.testTrueTarget = line.substringAfter("monkey").trimThanToInt()
            line.startsWith("If false:") -> monkey.testFalseTarget =
                line.substringAfter("monkey").trimThanToInt()

            else -> throw IllegalArgumentException("Unsupported input: [$line]")
        }
    }
    return monkeys
}

private data class Operation(val equanation: String) {
    fun process(old: Long) = calculate(operand, getInt(first, old), getInt(second, old))

    private fun calculate(operand: Char, a: Long, b: Long) = when (operand) {
        '+' -> a + b
        '-' -> a - b
        '/' -> a / b
        '*' -> (a * b).also { if (it < a) throw IllegalArgumentException("$a * $b != $it") }
        else -> throw IllegalArgumentException("Unsupported operand: [$operand]")
    }

    private fun getInt(string: String, old: Long) = if (string == "old") old else string.toLong()

    val components = equanation.trim().split(' ')
    val first = components.component3()
    val operand = components.component4().first()
    val second = components.component5()
}

private class Monkey(val id: Int) {

    var testFalseTarget: Int = -1
    var testTrueTarget: Int = -1
    var divisibleBy: Int = -1
    lateinit var operation: Operation
    lateinit var worryLevels: MutableList<Long>

    var inspectCount = 0L

    fun test(worryLevel: Long): Boolean {
        inspectCount++
        return (worryLevel % divisibleBy) == 0L
    }

    fun getTarget(worryLevel: Long) = if (test(worryLevel)) testTrueTarget else testFalseTarget
    override fun toString(): String {
        return "Monkey(id=$id, inspectCount=$inspectCount)"
    }
}

private fun String.trimThanToInt() = this.trim().toInt()

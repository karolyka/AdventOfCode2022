import java.io.File

const val INPUT_FILE_NAME = "day05/input.txt"
//const val INPUT_FILE_NAME = "day05/input_test.txt"

fun main(args: Array<String>) {
    val lines = File(INPUT_FILE_NAME).readLines()

    val params = lines.toParams()

    println(params.steps)

    calculate(params) { step, elfStacks -> elfStacks[step.to].push(elfStacks[step.from].pop(step.amount)) }

    calculate(params) { step, elfStacks -> elfStacks[step.to].push(elfStacks[step.from].popInOrder(step.amount)) }
}

private fun calculate(params: Params, function: (Step, Array<ElfStack<Char>>) -> Unit) {
    val queues = getQueues(params.columns)
    fillQueues(params.lines, params.indexOfFirst, queues)
    displayQueues(queues)

    params.steps.forEach { step -> function(step, queues) }
    displayQueues(queues)
    println(queues.map { it.getTop() }.joinToString(""))
}

private fun displayQueues(queues: Array<ElfStack<Char>>) {
    queues.forEach { println(it) }
}

private fun fillQueues(
    lines: List<String>,
    indexOfFirst: Int,
    queues: Array<ElfStack<Char>>
) {
    lines.subList(0, indexOfFirst)
        .map { line ->
            line.chunked(4)
                .map { it[1] }.forEachIndexed { index, c -> if (c != ' ') queues[index].put(c) }
        }
}

private fun getQueues(columns: Int) = (0 until columns).map { ElfStack<Char>() }.toTypedArray()

private class ElfStack<T> {
    private val elements = mutableListOf<T>()

    fun put(element: T) {
        elements.add(element)
    }

    fun pop(count: Int): List<T> = popInOrder(count).asReversed()
    fun popInOrder(count: Int): List<T> = (0 until count).map { elements.removeFirst() }

    fun push(elements: List<T>) {
        this.elements.addAll(0, elements)
    }

    fun getTop() = elements.first()

    override fun toString() = elements.toString()
}

private data class Step(val amount: Int, val from: Int, val to: Int)

private fun String.toStep() = this.split(' ').let { Step(it[1].toInt(), it[3].toInt() - 1, it[5].toInt() - 1) }

private data class Params(
    val columns: Int,
    val lines: List<String>,
    val indexOfFirst: Int,
    val steps: List<Step>
)

private fun List<String>.toParams(): Params {
    val indexOfFirst = indexOfFirst { it.startsWith(" 1") }
    val columns = this[indexOfFirst].split("""\W+""".toRegex()).count { it.isNotBlank() }

    val steps = subList(indexOfFirst + 2, size).map { it.toStep() }
    return Params(columns, this, indexOfFirst, steps)
}
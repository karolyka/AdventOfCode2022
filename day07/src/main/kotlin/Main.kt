import java.io.File

const val INPUT_FILE_NAME = "day07/input.txt"
//const val INPUT_FILE_NAME = "day07/input_test.txt"

private const val CMD_DELIMITER = ' '
private const val CMD_PREFIX = "$$CMD_DELIMITER"
private const val CMD_CD = "cd"
private const val CMD_LS = "ls"

private const val CD_ROOT = "/"
private const val CD_BACK = ".."

private const val TASK1_LIMIT = 100_000
private const val DRIVE_CAPACITY = 70_000_000
private const val SPACE_NEEDED = 30_000_000

fun main() {
    val lines = File(INPUT_FILE_NAME).readLines()

    val root = ElfDirectory(CD_ROOT, null)
    val elfDirectories = mutableListOf(root)
    var currentDirectory = root

    lines.forEach { line ->
        when {
            line.startsWith(CMD_PREFIX) ->
                when (line.getCommand()) {
                    CMD_CD -> line.getCDCommandArgument().let { name ->
                        currentDirectory = when (name) {
                            CD_ROOT -> root
                            CD_BACK -> currentDirectory.parent ?: root
                            else -> getOrCreateElfDirectory(currentDirectory, name, elfDirectories)
                        }
                    }

                    CMD_LS -> {}
                    else -> TODO("Unsupported command")
                }

            else -> {
                line.getSizeOrDirectoryName().toIntOrNull()?.let {
                    gerOrCreateElfFile(currentDirectory, line.getFileName(), it)
                }
            }
        }
    }

    println("Task1")
    println(elfDirectories.filter { it.size <= TASK1_LIMIT }.sumOf { it.size })

    println("\nTask2")
    val spaceNeeded = SPACE_NEEDED - DRIVE_CAPACITY + root.size
    println("Space needed: $spaceNeeded")
    println(elfDirectories.filter { it.size >= spaceNeeded }.minBy { it.size }.size)
}

private fun gerOrCreateElfFile(currentDirectory: ElfDirectory, name: String, size: Int) = currentDirectory.children
    .filterIsInstance<ElfFile>()
    .firstOrNull { it.name == name }
    ?: ElfFile(name, currentDirectory, size)
        .also { currentDirectory.children.add(it) }

private fun getOrCreateElfDirectory(
    currentDirectory: ElfDirectory,
    name: String,
    elfDirectories: MutableList<ElfDirectory>
) = (currentDirectory.children
    .filterNot { it is ElfFile }
    .firstOrNull { it.name == name }
    ?: ElfDirectory(name, currentDirectory)
        .also {
            currentDirectory.children.add(it)
            elfDirectories.add(it)
        })

private open class ElfDirectory(val name: String, open val parent: ElfDirectory?) {
    val children = mutableListOf<ElfDirectory>()
    open val size: Int
        get() = children.sumOf { it.size }

    override fun toString(): String {
        return "ElfDirectory(name='$name', size=$size)"
    }
}

private class ElfFile(name: String, override val parent: ElfDirectory, override var size: Int) :
    ElfDirectory(name, parent)

private fun String.getCommand() = this.substringAfter(CMD_PREFIX).substringBefore(CMD_DELIMITER)
private fun String.getCDCommandArgument() = this.substringAfter(CMD_CD).trim()

private fun String.getSizeOrDirectoryName() = this.substringBefore(CMD_DELIMITER)
private fun String.getFileName() = this.substringAfter(CMD_DELIMITER)

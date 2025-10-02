package dev.ktool.gen

const val LINE_SEPARATOR = "\n"

interface Writable {
    fun write(writer: CodeWriter)
    fun render(): String = CodeWriter().apply { write(this) }.toString()
}

class CodeWriter(indentationSize: Int = 4) {
    private val indentation = " ".repeat(indentationSize)
    private val lines = mutableListOf(StringBuilder())
    private var indentLevel = 0


    /**
     * Appends the given code to the current line.
     */
    fun write(code: String): CodeWriter = apply {
        if (code.contains(LINE_SEPARATOR)) {
            write(code.split(LINE_SEPARATOR))
        } else {
            lines.last().append(code)
        }
    }

    /**
     * Appends a list of lines adjusted the indentation as needed
     */
    fun write(codeLines: Collection<String>) = apply {
        if (codeLines.isEmpty()) return this

        val theLines = codeLines.toMutableList()

        if (lines.last().isNotBlank()) {
            lines.last().append(theLines.first())
            newLine()
            theLines.removeFirst()
        }

        theLines.joinToString(LINE_SEPARATOR).trimIndent().split(LINE_SEPARATOR).forEach {
            lines.last().append(it)
            newLine()
        }
    }

    /**
     * Appends the given code to the current line and starts a new line and adds the appropriate indentation.
     */
    fun newLine(code: String = "") = apply {
        lines.add(StringBuilder())
        if (indentLevel > 0) {
            lines.last().append(indentation.repeat(indentLevel))
        }
        write(code)
    }

    /**
     * Removes the last indentation from the current line.
     */
    fun removeLastIndentation() = apply {
        if (lines.last().endsWith(indentation)) {
            lines.last().setLength(lines.last().length - indentation.length)
        }
    }

    /**
     * If the last line is blank, removes it. Then trims the end of the last line.
     */
    fun trimEnd() = apply {
        if (lines.last().isBlank()) {
            lines.removeLast()
        }
        lines.last().trimEnd()
    }

    /**
     * Increases the indentation level by one, which weill be used for the next line.
     */
    fun indent() = apply { indentLevel++ }

    /**
     * Decreases the indentation level by one, which weill be used for the next line.
     */
    fun unindent() = apply { indentLevel-- }

    /**
     * Executes the given action with the indentation level increased by one, then decreases it back to the original level.
     */
    fun withIndent(action: CodeWriter.() -> Unit) = apply {
        indent()
        action()
        if (lines.last().isBlank()) {
            removeLastIndentation()
        }
        unindent()
    }

    override fun toString(): String = lines.joinToString(LINE_SEPARATOR) { it.toString().trimEnd() }
}

private val identifierRegex = Regex("[a-zA-Z_][a-zA-Z0-9_]*")

val String.safe: String get() = if (isKeyword() || !matches(identifierRegex)) "`$this`" else this

val String.safePackage: String get() = split(".").joinToString(".") { it.safe }.replace("`*`", "*")

fun String.isKeyword() = this in KEYWORDS

private val KEYWORDS = listOf(
    "class",
    "var",
    "val",
    "object",
    "if",
    "else",
    "when",
    "while",
    "for",
    "do",
    "try",
    "throw",
    "return",
    "break",
    "continue",
    "in",
    "is",
    "as",
    "null",
    "true",
    "false",
    "this",
    "super",
)

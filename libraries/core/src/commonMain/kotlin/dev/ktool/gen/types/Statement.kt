package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.Writable

interface Statement : Writable

fun String.toStatement(): Statement = Literal(this)

class Literal(private val value: String) : Statement, TopLevelDeclaration, ClassMember {
    constructor(block: CodeWriter.() -> Unit) : this(CodeWriter().apply(block).toString())

    override fun write(writer: CodeWriter) {
        writer.write(listOf(value))
    }

    override fun toString() = render()
}

class FunctionCall(
    val name: String,
    val args: List<Any> = listOf(),
    val target: String? = null,
) : Statement {
    override fun write(writer: CodeWriter) {
        target?.let { writer.write(it).write(".") }
        writer.write(name)

        if (args.lastOrNull() is Block) {
            if (args.size > 1) {
                writer.write(args.take(args.size - 1).joinToString(", ", prefix = "(", postfix = ")"))
            }
            (args.last() as Block).write(writer)
        } else {
            writer.write(args.joinToString(", ", prefix = "(", postfix = ")"))
        }
        writer.newLine()
    }

    override fun toString() = render()
}

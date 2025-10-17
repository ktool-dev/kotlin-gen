package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.Writable

open class Block(statements: List<Statement> = listOf(), block: Block.() -> Unit = {}) : Writable {
    constructor(vararg statements: Statement) : this(statements.toList())
    constructor(vararg statement: String) : this(statement.toList().map { it.toStatement() })

    val statements: MutableList<Statement> = statements.toMutableList()

    init {
        block()
    }

    operator fun Statement.unaryPlus() = apply { statements += this }
    operator fun String.unaryPlus() = apply { statements += toStatement() }

    override fun write(writer: CodeWriter) {
        writer.write(" {")
        writer.withIndent {
            newLine()
            statements.forEach { it.write(writer) }
        }
        writer.write("}")
    }

    override fun toString() = render()
}

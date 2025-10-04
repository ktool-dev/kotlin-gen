package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.Writable

open class Block(statements: List<Statement> = listOf()) : Writable {
    constructor(vararg statements: Statement) : this(statements.toList())
    constructor(vararg statement: String) : this(statement.toList().map { it.toStatement() })

    var statements: MutableList<Statement> = statements.toMutableList()

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

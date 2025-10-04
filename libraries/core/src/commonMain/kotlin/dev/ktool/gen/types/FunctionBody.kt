package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.Writable

sealed interface FunctionBody : Writable

class FunctionBlock(statements: List<Statement> = mutableListOf()) : FunctionBody, Block(statements) {
    constructor(vararg statements: Statement) : this(statements.toList())
    constructor(vararg statement: String) : this(statement.toList().map { it.toStatement() })
    constructor(block: CodeWriter.() -> Unit) : this(CodeWriter().apply(block).toString())
}

class ExpressionBody(var expression: String) : FunctionBody {
    constructor(block: CodeWriter.() -> Unit) : this(CodeWriter().apply(block).toString())

    override fun write(writer: CodeWriter) {
        writer.write(" = ")
        writer.write(expression)
    }
}
package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.Writable

sealed interface FunctionBody : Writable

class FunctionBlock(statements: List<Statement> = mutableListOf()) : FunctionBody, Block(statements) {
    constructor(vararg statements: Statement) : this(statements.toList())
    constructor(vararg statement: String) : this(statement.toList().map { it.toStatement() })
}

class ExpressionBody(var expression: String) : FunctionBody {
    override fun write(writer: CodeWriter) {
        writer.write(" = ")
        writer.write(expression)
    }
}
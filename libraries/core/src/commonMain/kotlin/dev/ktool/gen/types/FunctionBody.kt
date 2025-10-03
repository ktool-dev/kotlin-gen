package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.Writable

sealed interface FunctionBody : Writable

class FunctionBlock(statements: List<String> = mutableListOf()) : FunctionBody, Block(statements) {
    constructor(vararg statements: String) : this(statements.toList())
}

class ExpressionBody(var expression: String) : FunctionBody {
    override fun write(writer: CodeWriter) {
        writer.write(" = ")
        writer.write(expression)
    }
}
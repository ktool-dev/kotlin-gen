package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter

class InitBlock(statements: List<Statement> = listOf(), init: InitBlock.() -> Unit = {}) : ClassMember,
    Block(statements) {
    constructor(vararg statements: Statement) : this(statements.toList())
    constructor(vararg statement: String) : this(statement.toList().map { it.toStatement() })

    init {
        init()
    }

    override fun write(writer: CodeWriter) {
        writer.write("init")
        super.write(writer)
    }
}

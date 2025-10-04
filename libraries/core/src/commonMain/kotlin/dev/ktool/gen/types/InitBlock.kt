package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter

class InitBlock(statements: List<Statement> = listOf()) : ClassMember, Block(statements) {
    constructor(vararg statements: Statement) : this(statements.toList())
    constructor(vararg statement: String) : this(statement.toList().map { it.toStatement() })

    fun statement(vararg statements: Statement) {
        this.statements.addAll(statements)
    }

    fun statement(vararg statements: String) {
        statement(*statements.map { it.toStatement() }.toTypedArray())
    }

    override fun write(writer: CodeWriter) {
        writer.write("init")
        super.write(writer)
    }
}

interface InitBlocks {
    val members: MutableList<ClassMember>

    fun init(vararg statements: String) {
        members += InitBlock(*statements)
    }
}

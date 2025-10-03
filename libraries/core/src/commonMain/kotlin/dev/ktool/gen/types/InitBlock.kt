package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter

class InitBlock(statements: List<String> = listOf()) : ClassMember, Block(statements) {
    constructor(vararg statements: String) : this(statements.toList())

    fun add(statement: String) {
        statements.add(statement)
    }

    override fun write(writer: CodeWriter) {
        writer.write("init")
        super.write(writer)
    }
}

interface InitBlocks {
    val members: MutableList<ClassMember>

    fun init(vararg statements: String) {
        members += InitBlock(statements.asList())
    }
}

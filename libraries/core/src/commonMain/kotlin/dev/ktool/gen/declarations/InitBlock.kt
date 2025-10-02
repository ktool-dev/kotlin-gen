package dev.ktool.gen.declarations

import dev.ktool.gen.CodeWriter

class InitBlock(statements: MutableList<String> = mutableListOf()) : ClassMember, Block(statements) {
    override fun write(writer: CodeWriter) {
        writer.write("init")
        super.write(writer)
    }
}
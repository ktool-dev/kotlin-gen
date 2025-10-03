package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.Writable

open class Block(statements: List<String> = listOf()) : Writable {
    constructor(vararg statements: String) : this(statements.toList())

    var statements: MutableList<String> = statements.toMutableList()

    override fun write(writer: CodeWriter) {
        writer.write(" {")
        writer.withIndent {
            newLine()
            writer.write(statements)
        }
        writer.write("}")
    }
}

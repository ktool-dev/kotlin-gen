package dev.ktool.gen.declarations

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.Writable

open class Block(statements: List<String> = listOf()) : Writable {
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
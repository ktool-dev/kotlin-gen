package dev.ktool.gen.declarations

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.types.Modifier
import dev.ktool.gen.types.Parameter
import dev.ktool.gen.types.write

class Constructor(
    modifiers: List<Modifier> = listOf(),
    parameters: List<Parameter> = listOf(),
    var body: Block? = null
) : ClassMember {
    var modifiers: MutableList<Modifier> = modifiers.toMutableList()
    var parameters: MutableList<Parameter> = parameters.toMutableList()

    override fun write(writer: CodeWriter) {
        modifiers.write(writer)
        writer.write("constructor")
        parameters.write(writer)
        body?.write(writer)
    }
}
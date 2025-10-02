package dev.ktool.gen.declarations

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.Writable
import dev.ktool.gen.types.Modifier
import dev.ktool.gen.types.Parameter
import dev.ktool.gen.types.write

class PrimaryConstructor(
    modifiers: List<Modifier> = listOf(),
    parameters: List<Parameter> = listOf(),
    properties: List<Property> = listOf(),
) : Writable {
    var modifiers: MutableList<Modifier> = modifiers.toMutableList()
    var parameters: MutableList<Parameter> = parameters.toMutableList()
    var properties: MutableList<Property> = properties.toMutableList()

    override fun write(writer: CodeWriter) {
        if (modifiers.isEmpty() && parameters.isEmpty() && properties.isEmpty()) return

        if (modifiers.isNotEmpty()) {
            writer.write(" ")
            modifiers.write(writer)
            writer.write("constructor")
        }

        writer.write("(")
        parameters.forEachIndexed { index, param ->
            if (index > 0) writer.write(", ")
            param.write(writer)
        }
        properties.forEachIndexed { index, param ->
            if (parameters.isNotEmpty() || index > 0) writer.write(", ")
            param.write(writer)
        }
        writer.write(")")
    }
}
package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.Writable

class PrimaryConstructor(
    modifiers: List<Modifier> = listOf(),
    parameters: List<Parameter> = listOf(),
    properties: List<Property> = listOf(),
    block: PrimaryConstructor.() -> Unit = {}
) : Writable {
    val modifiers: MutableList<Modifier> = modifiers.toMutableList()
    val parameters: MutableList<Parameter> = parameters.toMutableList()
    val properties: MutableList<Property> = properties.toMutableList()

    init {
        block()
    }

    operator fun Modifier.unaryPlus() = apply { modifiers += this }
    operator fun List<Modifier>.unaryPlus() = apply { modifiers += this }
    operator fun Parameter.unaryPlus() = apply { parameters += this }
    operator fun Property.unaryPlus() = apply { properties += this }

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
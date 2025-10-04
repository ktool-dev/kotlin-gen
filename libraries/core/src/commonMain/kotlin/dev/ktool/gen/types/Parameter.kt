package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.Writable
import dev.ktool.gen.safe

class Parameter(
    var name: String,
    var type: Type,
    var defaultValue: ExpressionBody? = null,
    modifiers: List<Modifier> = listOf(),
) : Writable, Modifiers {
    constructor(name: String, type: Type, defaultValue: ExpressionBody? = null, block: Parameter.() -> Unit) : this(
        name,
        type,
        defaultValue
    ) {
        block()
    }

    override val modifiers: MutableList<Modifier> = modifiers.toMutableList()

    override fun write(writer: CodeWriter) {
        modifiers.write(writer)
        writer.write("${name.safe}: ")
        type.write(writer)
        defaultValue?.write(writer)
    }
}

fun List<Parameter>.write(writer: CodeWriter, nothingIfEmpty: Boolean = false) {
    if (nothingIfEmpty && isEmpty()) return

    writer.write("(")
    forEachIndexed { index, param ->
        if (index > 0) writer.write(", ")
        param.write(writer)
    }
    writer.write(")")
}

interface Parameters {
    val parameters: MutableList<Parameter>

    fun param(name: String, type: Type, returnType: ExpressionBody? = null, block: Parameter.() -> Unit = {}) {
        parameters += Parameter(name, type, returnType, block)
    }
}

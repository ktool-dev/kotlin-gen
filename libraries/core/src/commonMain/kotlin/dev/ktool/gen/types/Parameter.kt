package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.Writable
import dev.ktool.gen.declarations.ExpressionBody
import dev.ktool.gen.safe

class Parameter(
    var name: String,
    var type: Type,
    modifiers: List<Modifier> = listOf(),
    var defaultValue: ExpressionBody? = null
) : Writable {
    var modifiers: MutableList<Modifier> = modifiers.toMutableList()


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
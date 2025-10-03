package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.safe

class TypeAlias(
    var name: String,
    var type: Type,
    modifiers: List<Modifier> = listOf(),
    typeParameters: List<TypeParameter> = listOf()
) : TopLevelDeclaration, Modifiers, TypeParameters {
    constructor(name: String, type: Type, block: TypeAlias.() -> Unit) : this(name, type) {
        block()
    }

    override val modifiers: MutableList<Modifier> = modifiers.toMutableList()
    override val typeParameters: MutableList<TypeParameter> = typeParameters.toMutableList()

    override fun write(writer: CodeWriter) {
        modifiers.write(writer)
        writer.write("typealias ${name.safe}")
        typeParameters.write(writer)
        writer.write(" = ")
        type.write(writer)
    }
}

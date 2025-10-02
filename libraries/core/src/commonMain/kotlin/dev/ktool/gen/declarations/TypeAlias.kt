package dev.ktool.gen.declarations

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.safe
import dev.ktool.gen.types.Modifier
import dev.ktool.gen.types.Type
import dev.ktool.gen.types.TypeParameter
import dev.ktool.gen.types.write

class TypeAlias(
    var name: String,
    var type: Type,
    modifiers: List<Modifier> = listOf(),
    typeParameters: List<TypeParameter> = listOf()
) : TopLevelDeclaration {
    var modifiers: MutableList<Modifier> = modifiers.toMutableList()
    var typeParameters: MutableList<TypeParameter> = typeParameters.toMutableList()

    override fun write(writer: CodeWriter) {
        modifiers.write(writer)
        writer.write("typealias ${name.safe}")
        typeParameters.write(writer)
        writer.write(" = ")
        type.write(writer)
    }
}

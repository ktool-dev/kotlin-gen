package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.safe

class TypeAlias(
    var name: String,
    var type: Type,
    modifiers: List<Modifier> = listOf(),
    typeParameters: List<TypeParameter> = listOf(),
    block: TypeAlias.() -> Unit = {},
) : TopLevelDeclaration {
    val modifiers: MutableList<Modifier> = modifiers.toMutableList()
    val typeParameters: MutableList<TypeParameter> = typeParameters.toMutableList()

    init {
        block()
    }

    operator fun Modifier.unaryPlus() = apply { modifiers += this }
    operator fun List<Modifier>.unaryPlus() = apply { modifiers += this }
    operator fun TypeParameter.unaryPlus() = apply { typeParameters += this }

    override fun write(writer: CodeWriter) {
        modifiers.write(writer)
        writer.write("typealias ${name.safe}")
        typeParameters.write(writer)
        writer.write(" = ")
        type.write(writer)
    }
}

package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.safe

class Interface(
    var name: String,
    modifiers: List<Modifier> = listOf(),
    typeParameters: List<TypeParameter> = listOf(),
    superTypes: List<Type> = listOf(),
    members: List<ClassMember> = listOf(),
    block: Interface.() -> Unit = {}
) : TopLevelDeclaration, ClassMember {
    val modifiers: MutableList<Modifier> = modifiers.toMutableList()
    val typeParameters: MutableList<TypeParameter> = typeParameters.toMutableList()
    val superTypes: MutableList<Type> = superTypes.toMutableList()
    val members: MutableList<ClassMember> = members.toMutableList()

    init {
        block()
    }

    operator fun Modifier.unaryPlus() = apply { modifiers += this }
    operator fun List<Modifier>.unaryPlus() = apply { modifiers += this }
    operator fun TypeParameter.unaryPlus() = apply { typeParameters += this }
    operator fun SuperType.unaryPlus() = apply { superTypes += this }
    operator fun ClassMember.unaryPlus() = apply { members += this }

    fun literal(code: String) {
        members += Literal(code)
    }

    override fun write(writer: CodeWriter) {
        modifiers.write(writer)
        writer.write("interface ${name.safe}")
        typeParameters.write(writer)
        superTypes.writeSuperTypes(writer)
        members.write(writer, nothingIfEmpty = true)
    }
}

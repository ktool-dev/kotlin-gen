package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.safe

class Object(
    var name: String,
    modifiers: List<Modifier> = listOf(),
    superTypes: List<Type> = listOf(),
    members: List<ClassMember> = listOf(),
    block: Object.() -> Unit = {},
) : TopLevelDeclaration, ClassMember {
    val modifiers: MutableList<Modifier> = modifiers.toMutableList()
    val superTypes: MutableList<Type> = superTypes.toMutableList()
    val members: MutableList<ClassMember> = members.toMutableList()

    init {
        block()
    }

    operator fun Modifier.unaryPlus() = apply { modifiers += this }
    operator fun List<Modifier>.unaryPlus() = apply { modifiers += this }
    operator fun SuperType.unaryPlus() = apply { superTypes += this }
    operator fun ClassMember.unaryPlus() = apply { members += this }

    override fun write(writer: CodeWriter) {
        modifiers.write(writer)
        writer.write("object ${name.safe}")
        superTypes.writeSuperTypes(writer)
        members.write(writer, nothingIfEmpty = true)
    }
}

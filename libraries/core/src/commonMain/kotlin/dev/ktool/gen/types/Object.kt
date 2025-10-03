package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.safe

class Object(
    var name: String,
    modifiers: List<Modifier> = listOf(),
    superTypes: List<Type> = listOf(),
    members: List<ClassMember> = listOf()
) : TopLevelDeclaration, ClassMember, Modifiers, SuperTypes, TypeMembers, InitBlocks {
    constructor(name: String, block: Object.() -> Unit) : this(name) {
        block()
    }

    override val modifiers: MutableList<Modifier> = modifiers.toMutableList()
    override val superTypes: MutableList<Type> = superTypes.toMutableList()
    override val members: MutableList<ClassMember> = members.toMutableList()

    override fun write(writer: CodeWriter) {
        modifiers.write(writer)
        writer.write("object ${name.safe}")
        superTypes.writeSuperTypes(writer)
        members.write(writer, nothingIfEmpty = true)
    }
}

interface Objects {
    val members: MutableList<ClassMember>

    fun obj(name: String, block: Object.() -> Unit = {}) {
        members += Object(name, block)
    }
}

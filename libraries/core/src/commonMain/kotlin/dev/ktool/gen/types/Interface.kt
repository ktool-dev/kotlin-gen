package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.safe

class Interface(
    var name: String,
    modifiers: List<Modifier> = listOf(),
    typeParameters: List<TypeParameter> = listOf(),
    superTypes: List<Type> = listOf(),
    members: List<ClassMember> = listOf()
) : TopLevelDeclaration, ClassMember, Modifiers, TypeParameters, SuperTypes, TypeMembers {
    constructor(name: String, block: Interface.() -> Unit) : this(name) {
        block()
    }

    override val modifiers: MutableList<Modifier> = modifiers.toMutableList()
    override val typeParameters: MutableList<TypeParameter> = typeParameters.toMutableList()
    override val superTypes: MutableList<Type> = superTypes.toMutableList()
    override val members: MutableList<ClassMember> = members.toMutableList()

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

interface Interfaces {
    val members: MutableList<ClassMember>

    fun inter(name: String, block: Interface.() -> Unit = {}) {
        members += Interface(name, block)
    }
}

interface TypeMembers : Properties, Objects, Classes, Interfaces, Functions

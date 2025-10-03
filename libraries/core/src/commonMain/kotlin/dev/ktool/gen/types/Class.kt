package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.safe

class Class(
    var name: String,
    var primaryConstructor: PrimaryConstructor? = null,
    modifiers: List<Modifier> = listOf(),
    typeParameters: List<TypeParameter> = listOf(),
    superTypes: List<Type> = listOf(),
    members: List<ClassMember> = listOf()
) : TopLevelDeclaration, Modifiers, TypeParameters, ClassMember, SuperTypes, TypeMembers, InitBlocks,
    Objects, Constructors {
    constructor(name: String, primaryConstructor: PrimaryConstructor? = null, block: Class.() -> Unit) : this(
        name,
        primaryConstructor
    ) {
        block()
    }

    override val modifiers: MutableList<Modifier> = modifiers.toMutableList()
    override val typeParameters: MutableList<TypeParameter> = typeParameters.toMutableList()
    override val superTypes: MutableList<Type> = superTypes.toMutableList()
    override val members: MutableList<ClassMember> = members.toMutableList()

    override fun write(writer: CodeWriter) {
        modifiers.write(writer)
        writer.write("class ${name.safe}")
        typeParameters.write(writer)
        primaryConstructor?.write(writer)
        superTypes.writeSuperTypes(writer)
        members.write(writer, nothingIfEmpty = true)
    }
}

fun List<Type>.writeSuperTypes(writer: CodeWriter) {
    if (isNotEmpty()) {
        writer.write(" : ")
        forEachIndexed { index, superType ->
            if (index > 0) writer.write(", ")
            superType.write(writer)
        }
    }
}

interface Classes {
    val members: MutableList<ClassMember>

    fun clas(name: String, primaryConstructor: PrimaryConstructor? = null, block: Class.() -> Unit = {}) {
        members += Class(name, primaryConstructor, block)
    }
}
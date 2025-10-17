package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.safe

class Class(
    var name: String,
    var primaryConstructor: PrimaryConstructor? = null,
    modifiers: List<Modifier> = listOf(),
    typeParameters: List<TypeParameter> = listOf(),
    superTypes: List<SuperType> = listOf(),
    members: List<ClassMember> = listOf(),
    block: Class.() -> Unit = {},
) : TopLevelDeclaration, ClassMember {
    val modifiers: MutableList<Modifier> = modifiers.toMutableList()
    val typeParameters: MutableList<TypeParameter> = typeParameters.toMutableList()
    val superTypes: MutableList<SuperType> = superTypes.toMutableList()
    val members: MutableList<ClassMember> = members.toMutableList()

    init {
        block()
    }

    operator fun Modifier.unaryPlus() = apply { modifiers += this }
    operator fun List<Modifier>.unaryPlus() = apply { modifiers += this }
    operator fun TypeParameter.unaryPlus() = apply { typeParameters += this }
    operator fun SuperType.unaryPlus() = apply { superTypes += this }
    operator fun ClassMember.unaryPlus() = apply { members += this }

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

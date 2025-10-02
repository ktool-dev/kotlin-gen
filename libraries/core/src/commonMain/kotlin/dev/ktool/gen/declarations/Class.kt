package dev.ktool.gen.declarations

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.safe
import dev.ktool.gen.types.Modifier
import dev.ktool.gen.types.Type
import dev.ktool.gen.types.TypeParameter
import dev.ktool.gen.types.write

class Class(
    var name: String,
    var primaryConstructor: PrimaryConstructor? = null,
    modifiers: List<Modifier> = listOf(),
    typeParameters: List<TypeParameter> = listOf(),
    superTypes: List<Type> = listOf(),
    members: List<ClassMember> = listOf()
) : TopLevelDeclaration {
    var modifiers: MutableList<Modifier> = modifiers.toMutableList()
    var typeParameters: MutableList<TypeParameter> = typeParameters.toMutableList()
    var superTypes: MutableList<Type> = superTypes.toMutableList()
    var members: MutableList<ClassMember> = members.toMutableList()

    override fun write(writer: CodeWriter) {
        modifiers.write(writer)
        writer.write("class ${name.safe}")
        typeParameters.write(writer)
        primaryConstructor?.write(writer)
        superTypes.writeSuperTypes(writer)
        members.write(writer, nothingIfEmpty = true)
    }
}

fun List<TypeParameter>.write(writer: CodeWriter) {
    if (isNotEmpty()) {
        writer.write("<")
        forEachIndexed { index, typeParam ->
            if (index > 0) writer.write(", ")
            typeParam.write(writer)
        }
        writer.write(">")
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
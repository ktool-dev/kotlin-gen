package dev.ktool.gen.declarations

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.safe
import dev.ktool.gen.types.Modifier
import dev.ktool.gen.types.Type
import dev.ktool.gen.types.write

class Object(
    var name: String,
    modifiers: List<Modifier> = listOf(),
    superTypes: List<Type> = listOf(),
    members: List<ClassMember> = listOf()
) : TopLevelDeclaration {
    var modifiers: MutableList<Modifier> = modifiers.toMutableList()
    var superTypes: MutableList<Type> = superTypes.toMutableList()
    var members: MutableList<ClassMember> = members.toMutableList()
    
    override fun write(writer: CodeWriter) {
        modifiers.write(writer)
        writer.write("object ${name.safe}")
        superTypes.writeSuperTypes(writer)
        members.write(writer, nothingIfEmpty = true)
    }
}
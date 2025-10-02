package dev.ktool.gen.declarations

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.safe
import dev.ktool.gen.types.*

class Function(
    var name: String,
    var receiver: Type? = null,
    modifiers: List<Modifier> = listOf(),
    typeParameters: List<TypeParameter> = listOf(),
    parameters: List<Parameter> = listOf(),
    var returnType: Type? = null,
    var body: FunctionBody? = null
) : ClassMember, TopLevelDeclaration {
    var modifiers: MutableList<Modifier> = modifiers.toMutableList()
    var typeParameters: MutableList<TypeParameter> = typeParameters.toMutableList()
    var parameters: MutableList<Parameter> = parameters.toMutableList()


    override fun write(writer: CodeWriter) {
        modifiers.write(writer)
        writer.write("fun ")
        
        if (typeParameters.isNotEmpty()) {
            typeParameters.write(writer)
            writer.write(" ")
        }

        receiver?.let {
            it.write(writer)
            writer.write(".")
        }
        writer.write(name.safe)
        parameters.write(writer)

        returnType?.let {
            writer.write(": ")
            it.write(writer)
        }

        body?.write(writer)
    }
}
package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.safe

class Function(
    var name: String,
    var receiver: Type? = null,
    var returnType: Type? = null,
    var body: FunctionBody? = null,
    modifiers: List<Modifier> = listOf(),
    typeParameters: List<TypeParameter> = listOf(),
    parameters: List<Parameter> = listOf(),
) : ClassMember, TopLevelDeclaration, TypeParameters, Modifiers, Parameters {
    constructor(name: String, receiver: Type? = null, returnType: Type? = null, block: Function.() -> Unit) : this(
        name,
        receiver,
        returnType
    ) {
        block()
    }

    override var modifiers: MutableList<Modifier> = modifiers.toMutableList()
    override var typeParameters: MutableList<TypeParameter> = typeParameters.toMutableList()
    override var parameters: MutableList<Parameter> = parameters.toMutableList()

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

interface Functions {
    val members: MutableList<ClassMember>

    fun addFunction(name: String, receiver: Type? = null, returnType: Type? = null, block: Function.() -> Unit = {}) {
        members += Function(name, receiver, returnType, block)
    }
}

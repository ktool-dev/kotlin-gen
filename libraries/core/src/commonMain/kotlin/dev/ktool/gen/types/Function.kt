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
    block: Function.() -> Unit = {},
) : ClassMember, TopLevelDeclaration {
    val modifiers: MutableList<Modifier> = modifiers.toMutableList()
    val typeParameters: MutableList<TypeParameter> = typeParameters.toMutableList()
    val parameters: MutableList<Parameter> = parameters.toMutableList()

    init {
        block()
    }

    operator fun TypeParameter.unaryPlus() = apply { typeParameters += this }
    operator fun Parameter.unaryPlus() = apply { parameters += this }
    operator fun Modifier.unaryPlus() = apply { modifiers += this }
    operator fun List<Modifier>.unaryPlus() = apply { modifiers += this }

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

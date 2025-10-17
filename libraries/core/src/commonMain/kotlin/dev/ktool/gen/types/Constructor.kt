package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter

class Constructor(
    modifiers: List<Modifier> = listOf(),
    parameters: List<Parameter> = listOf(),
    var thisParams: List<String> = listOf(),
    var body: Block? = null,
    init: Constructor.() -> Unit = {},
) : ClassMember {
    val modifiers: MutableList<Modifier> = modifiers.toMutableList()
    val parameters: MutableList<Parameter> = parameters.toMutableList()

    init {
        init()
    }

    operator fun Modifier.unaryPlus() = apply { modifiers += this }
    operator fun List<Modifier>.unaryPlus() = apply { modifiers += this }
    operator fun Parameter.unaryPlus() = apply { parameters += this }

    override fun write(writer: CodeWriter) {
        modifiers.write(writer)
        writer.write("constructor")
        parameters.write(writer)
        writer.write(" : this(${thisParams.joinToString(", ")})")
        body?.write(writer)
    }
}

package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter

class Constructor(
    modifiers: List<Modifier> = listOf(),
    parameters: List<Parameter> = listOf(),
    var thisParams: List<String> = listOf(),
    var body: Block? = null,
) : ClassMember, Modifiers, Parameters {
    constructor(block: Constructor.() -> Unit) : this() {
        block()
    }

    override var modifiers: MutableList<Modifier> = modifiers.toMutableList()
    override var parameters: MutableList<Parameter> = parameters.toMutableList()

    override fun write(writer: CodeWriter) {
        modifiers.write(writer)
        writer.write("constructor")
        parameters.write(writer)
        writer.write(" : this(${thisParams.joinToString(", ")})")
        body?.write(writer)
    }
}

interface Constructors {
    val members: MutableList<ClassMember>

    fun constructor(block: Constructor.() -> Unit = {}) {
        members += Constructor(block)
    }
}

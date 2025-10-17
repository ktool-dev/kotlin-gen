package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.Writable
import dev.ktool.gen.safe

class Property(
    var name: String,
    var mutable: Boolean = false,
    var type: Type? = null,
    var initializer: ExpressionBody? = null,
    var getter: PropertyGetter? = null,
    var setter: PropertySetter? = null,
    modifiers: List<Modifier> = listOf(),
    block: Property.() -> Unit = {},
) : ClassMember, TopLevelDeclaration {
    val modifiers: MutableList<Modifier> = modifiers.toMutableList()

    init {
        block()
    }

    operator fun Modifier.unaryPlus() = apply { modifiers += this }
    operator fun List<Modifier>.unaryPlus() = apply { modifiers += this }

    init {
        if (type == null && initializer == null) {
            error("Properties must have a type or an initializer")
        }
    }

    override fun write(writer: CodeWriter) {
        modifiers.write(writer)
        if (mutable) writer.write("var ") else writer.write("val ")
        writer.write(name.safe)

        type?.let {
            writer.write(": ")
            it.write(writer)
        }

        initializer?.write(writer)

        if (getter != null || setter != null) {
            writer.withIndent {
                getter?.write(writer)
                setter?.write(writer)
            }
        }
    }
}

class PropertyGetter(
    modifiers: List<Modifier> = listOf(),
    var body: FunctionBody
) : Writable {
    private val modifiers: MutableList<Modifier> = modifiers.toMutableList()

    operator fun Modifier.unaryPlus() = apply { modifiers += this }
    operator fun List<Modifier>.unaryPlus() = apply { modifiers += this }

    override fun write(writer: CodeWriter) {
        writer.newLine()
        writer.write("get()")
        body.write(writer)
    }
}

class PropertySetter(
    modifiers: List<Modifier> = listOf(),
    var body: Block,
    var paramName: String = "newValue"
) : Writable {
    private val modifiers: MutableList<Modifier> = modifiers.toMutableList()

    operator fun Modifier.unaryPlus() = apply { modifiers += this }
    operator fun List<Modifier>.unaryPlus() = apply { modifiers += this }

    override fun write(writer: CodeWriter) {
        writer.newLine()
        writer.write("set(${paramName.safe})")
        body.write(writer)
    }
}

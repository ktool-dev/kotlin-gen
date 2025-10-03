package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.Writable
import dev.ktool.gen.safe

enum class Mutability {
    Val, Var;

    override fun toString(): String = name.lowercase()
}

class Property(
    var name: String,
    var mutability: Mutability = Mutability.Val,
    var type: Type? = null,
    var initializer: ExpressionBody? = null,
    var getter: PropertyGetter? = null,
    var setter: PropertySetter? = null,
    modifiers: List<Modifier> = listOf(),
) : ClassMember, TopLevelDeclaration, Modifiers {
    constructor(
        name: String,
        mutability: Mutability = Mutability.Val,
        type: Type? = null,
        initializer: ExpressionBody? = null,
        block: Property.() -> Unit
    ) : this(name = name, mutability = mutability, type = type, initializer = initializer) {
        block()
    }

    override val modifiers: MutableList<Modifier> = modifiers.toMutableList()

    init {
        if (type == null && initializer == null) {
            error("Properties must have a type or an initializer")
        }
    }

    override fun write(writer: CodeWriter) {
        modifiers.write(writer)
        writer.write("$mutability ${name.safe}")

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
    val modifiers: MutableList<Modifier> = modifiers.toMutableList()

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
    val modifiers: MutableList<Modifier> = modifiers.toMutableList()

    override fun write(writer: CodeWriter) {
        writer.newLine()
        writer.write("set(${paramName.safe})")
        body.write(writer)
    }
}

interface Properties : SpecificProperties {
    val members: MutableList<ClassMember>

    override fun property(
        name: String,
        mutability: Mutability,
        type: Type?,
        initializer: ExpressionBody?,
        block: Property.() -> Unit,
    ) {
        members += Property(name, mutability, type, initializer, block)
    }
}

interface SpecificProperties {
    fun property(
        name: String,
        mutability: Mutability = Mutability.Val,
        type: Type? = null,
        initializer: ExpressionBody? = null,
        block: Property.() -> Unit = {},
    )

    fun valProperty(
        name: String,
        type: Type? = null,
        initializer: ExpressionBody? = null,
        block: Property.() -> Unit = {}
    ) {
        property(name, Mutability.Val, type, initializer, block)
    }

    fun varProperty(
        name: String,
        type: Type? = null,
        initializer: ExpressionBody? = null,
        block: Property.() -> Unit = {}
    ) {
        property(name, Mutability.Var, type, initializer, block)
    }
}

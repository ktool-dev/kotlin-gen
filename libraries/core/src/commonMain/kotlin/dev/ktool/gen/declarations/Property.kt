package dev.ktool.gen.declarations

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.Writable
import dev.ktool.gen.safe
import dev.ktool.gen.types.Modifier
import dev.ktool.gen.types.PropertyMutability
import dev.ktool.gen.types.Type
import dev.ktool.gen.types.write


class PropertyAccessor(
    modifiers: List<Modifier> = listOf(),
    var body: FunctionBody
) {
    var modifiers: MutableList<Modifier> = modifiers.toMutableList()
}

class Property(
    var name: String,
    modifiers: List<Modifier> = listOf(),
    var type: Type? = null,
    var initializer: ExpressionBody? = null,
    var getter: PropertyGetter? = null,
    var setter: PropertySetter? = null,
    var mutability: PropertyMutability = PropertyMutability.VAL
) : ClassMember, TopLevelDeclaration {
    var modifiers: MutableList<Modifier> = modifiers.toMutableList()


    init {
        if (type == null && initializer == null) {
            error("Properties must have a type or an initializer")
        }
    }

    override fun write(writer: CodeWriter) {
        modifiers.write(writer)
        writer.write("${mutability.keyword} ${name.safe}")

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
    var modifiers: MutableList<Modifier> = modifiers.toMutableList()


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
    var modifiers: MutableList<Modifier> = modifiers.toMutableList()


    override fun write(writer: CodeWriter) {
        writer.newLine()
        writer.write("set(${paramName.safe})")
        body.write(writer)
    }
}

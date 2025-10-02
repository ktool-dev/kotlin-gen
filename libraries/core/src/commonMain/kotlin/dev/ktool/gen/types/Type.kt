package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.Writable

val IntType: Type get() = Type("Int")
val FloatType: Type get() = Type("Float")
val DoubleType: Type get() = Type("Double")
val StringType: Type get() = Type("String")
val BooleanType: Type get() = Type("Boolean")
val EnumType: Type get() = Type("Enum")
val LongType: Type get() = Type("Long")

class Type(
    var name: String,
    typeArguments: List<Type> = listOf(),
    var nullable: Boolean = false
) : Writable {
    var typeArguments: MutableList<Type> = typeArguments.toMutableList()

    init {
        if (name.endsWith("?")) {
            name = name.removeSuffix("?")
            nullable = true
        }
    }

    override fun write(writer: CodeWriter) {
        writer.write(name)
        if (typeArguments.isNotEmpty()) {
            writer.write("<")
            typeArguments.forEachIndexed { index, type ->
                if (index > 0) writer.write(", ")
                type.write(writer)
            }
            writer.write(">")
        }
        if (nullable) {
            writer.write("?")
        }
    }
}
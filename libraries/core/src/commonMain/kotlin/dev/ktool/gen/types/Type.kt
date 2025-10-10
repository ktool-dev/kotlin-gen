package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.Writable
import kotlin.reflect.KClass

val IntType: Type get() = Type("Int")
val FloatType: Type get() = Type("Float")
val DoubleType: Type get() = Type("Double")
val StringType: Type get() = Type("String")
val BooleanType: Type get() = Type("Boolean")
val EnumType: Type get() = Type("Enum")
val LongType: Type get() = Type("Long")
val ShortType: Type get() = Type("Short")
val ByteType: Type get() = Type("Byte")
val CharType: Type get() = Type("Char")
val IntRangeType: Type get() = Type("IntRange")
val LongRangeType: Type get() = Type("LongRange")
val ShortRangeType: Type get() = Type("IntRange")
val ByteRangeType: Type get() = Type("IntRange")
val CharRangeType: Type get() = Type("IntRange")
val BooleanRangeType: Type get() = Type("Boolean")
val EnumRangeType: Type get() = Type("Enum")
val UnitType: Type get() = Type("Unit")

class Type(
    var name: String,
    var nullable: Boolean = false,
    typeArguments: List<Type> = listOf(),
) : WritableType, TypeArguments {
    constructor(name: String, vararg typeArgument: Type) : this(
        name,
        false,
        typeArgument.toList()
    )

    constructor(name: String, nullable: Boolean = false, vararg typeArgument: Type) : this(
        name,
        nullable,
        typeArgument.toList()
    )

    constructor(name: String, nullable: Boolean = false, block: Type.() -> Unit) : this(name, nullable) {
        block()
    }

    override var typeArguments: MutableList<Type> = typeArguments.toMutableList()

    init {
        if (name.endsWith("?")) {
            name = name.removeSuffix("?")
            nullable = true
        }
    }

    override fun write(writer: CodeWriter) {
        writer.write(name)
        typeArguments.write(writer)
        if (nullable) {
            writer.write("?")
        }
    }
}

interface WritableType : Writable

fun List<WritableType>.write(writer: CodeWriter) {
    if (isNotEmpty()) {
        writer.write("<")
        forEachIndexed { index, type ->
            if (index > 0) writer.write(", ")
            type.write(writer)
        }
        writer.write(">")
    }
}

interface TypeArguments {
    val typeArguments: MutableList<Type>

    fun addTypeArgument(name: String, nullable: Boolean = false, block: Type.() -> Unit = {}) {
        typeArguments.add(Type(name, nullable, block))
    }
}

interface SuperTypes {
    val superTypes: MutableList<Type>

    fun addSuperType(name: String, block: Type.() -> Unit = {}) {
        superTypes.add(Type(name, false, block))
    }
}

fun KClass<*>.createType(nullable: Boolean = false, typeArguments: List<Type> = listOf()): Type {
    val packageName = qualifiedName?.substringBeforeLast('.')
    val name = if (packageName in AUTO_IMPORTED_PACKAGES) {
        simpleName
    } else {
        qualifiedName
    } ?: error("Cannot create type from $this")
    return Type(name, nullable, typeArguments)
}

private val AUTO_IMPORTED_PACKAGES = setOf(
    "kotlin",
    "kotlin.annotation",
    "kotlin.collections",
    "kotlin.comparisons",
    "kotlin.coroutines",
    "kotlin.io",
    "kotlin.ranges",
    "kotlin.sequences",
    "kotin.text",
    "kotlin.math",
    "java.lang",
    "kotlin.jvm",
)

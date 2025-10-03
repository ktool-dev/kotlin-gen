package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.safe

class TypeParameter(
    var name: String,
    var variance: Variance? = null,
    typeArguments: List<Type> = listOf(),
) : WritableType, TypeArguments {
    constructor(name: String, variance: Variance? = null, block: TypeParameter.() -> Unit) : this(name, variance) {
        block()
    }

    override val typeArguments: MutableList<Type> = typeArguments.toMutableList()

    override fun write(writer: CodeWriter) {
        variance?.let { writer.write("$it ") }
        writer.write(name.safe)
        if (typeArguments.isNotEmpty()) {
            writer.write(" : ")
            typeArguments.forEachIndexed { index, bound ->
                if (index > 0) writer.write(", ")
                bound.write(writer)
            }
        }
    }
}

interface TypeParameters {
    val typeParameters: MutableList<TypeParameter>

    fun typeParam(name: String, variance: Variance? = null, block: TypeParameter.() -> Unit = {}) {
        typeParameters.add(TypeParameter(name, variance, block))
    }
}

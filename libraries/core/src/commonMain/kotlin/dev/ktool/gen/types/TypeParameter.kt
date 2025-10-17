package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.safe

class TypeParameter(
    var name: String,
    var variance: Variance? = null,
    typeArguments: List<TypeArgument> = listOf(),
    block: TypeParameter.() -> Unit = {},
) : WritableType {
    val typeArguments: MutableList<TypeArgument> = typeArguments.toMutableList()

    init {
        block()
    }

    operator fun TypeArgument.unaryPlus() = apply { typeArguments += this }

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

package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.Writable
import dev.ktool.gen.safe

class TypeParameter(
    var name: String,
    bounds: List<Type> = listOf(),
    var variance: Variance? = null
) : Writable {
    var bounds: MutableList<Type> = bounds.toMutableList()


    override fun write(writer: CodeWriter) {
        variance?.let { writer.write("$it ") }
        writer.write(name.safe)
        if (bounds.isNotEmpty()) {
            writer.write(" : ")
            bounds.forEachIndexed { index, bound ->
                if (index > 0) writer.write(", ")
                bound.write(writer)
            }
        }
    }
}
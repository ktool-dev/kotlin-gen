package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter

enum class Modifier {
    PUBLIC, PRIVATE, PROTECTED, INTERNAL,
    ABSTRACT, FINAL, OPEN, SEALED,
    OVERRIDE, VIRTUAL,
    STATIC, INLINE, SUSPEND,
    DATA, ENUM, ANNOTATION,
    VARARG, NOINLINE, CROSSINLINE,
    CONST, LATEINIT,
    OPERATOR, INFIX, TAILREC;

    override fun toString(): String = name.lowercase()
}

fun Collection<Modifier>.write(writer: CodeWriter) {
    if (isNotEmpty()) {
        writer.write(distinct().sorted().joinToString(" ", postfix = " ") { it.toString() })
    }
}

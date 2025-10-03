package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter

enum class Modifier {
    Public, Private, Protected, Internal,
    Abstract, Final, Open, Sealed,
    Override, Virtual,
    Static, Inline, Suspend,
    Data, Enum, Annotation,
    Vararg, Noinline, Crossinline,
    Const, Lateinit, Companion,
    Operator, Infix, Teilrec;

    override fun toString(): String = name.lowercase()
}

fun Collection<Modifier>.write(writer: CodeWriter) {
    if (isNotEmpty()) {
        writer.write(distinct().sorted().joinToString(" ", postfix = " ") { it.toString() })
    }
}

interface Modifiers {
    val modifiers: MutableList<Modifier>

    fun modifier(vararg m: Modifier) {
        modifiers.addAll(m.toList())
    }
}

package dev.ktool.gen.declarations

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.Writable

sealed interface ClassMember : Writable

fun List<ClassMember>.write(writer: CodeWriter, nothingIfEmpty: Boolean = false) {
    if (nothingIfEmpty && isEmpty()) return

    writer.write(" {")
    writer.withIndent {
        writer.newLine()
        var previousMember: ClassMember? = null
        forEach { member ->
            if (previousMember != null && (member !is Property || previousMember !is Property)) {
                writer.newLine()
            }
            member.write(writer)
            writer.newLine()
            previousMember = member
        }
    }
    writer.write("}")
}
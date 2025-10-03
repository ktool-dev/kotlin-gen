package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.Writable
import dev.ktool.gen.safe
import dev.ktool.gen.safePackage

class Import(
    var packagePath: String,
    var alias: String? = null
) : Writable {
    override fun write(writer: CodeWriter) {
        writer.write("import ${packagePath.safePackage}")
        alias?.let { writer.write(" as ${it.safe}") }
    }
}

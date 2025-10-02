package dev.ktool.gen.declarations

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.LINE_SEPARATOR
import dev.ktool.gen.Writable
import dev.ktool.gen.safePackage

class KotlinFile(
    var packageName: String?,
    imports: List<Import> = listOf(),
    topLevelDeclarations: List<TopLevelDeclaration> = listOf()
) : Writable {
    var imports: MutableList<Import> = imports.toMutableList()
    var topLevelDeclarations: MutableList<TopLevelDeclaration> = topLevelDeclarations.toMutableList()

    override fun write(writer: CodeWriter) {
        packageName?.let {
            writer.write("package ${it.safePackage}")
            writer.newLine()
            writer.newLine()
        }

        if (imports.isNotEmpty()) {
            imports.sortedBy { it.packagePath }.forEach { import ->
                import.write(writer)
                writer.newLine()
            }
            writer.newLine()
        }

        topLevelDeclarations.forEach {
            it.write(writer)
            writer.newLine(LINE_SEPARATOR)
        }
    }

    override fun render() = super.render().trim() + LINE_SEPARATOR
}
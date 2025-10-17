package dev.ktool.gen.types

import dev.ktool.gen.CodeWriter
import dev.ktool.gen.LINE_SEPARATOR
import dev.ktool.gen.Writable
import dev.ktool.gen.safePackage

sealed interface TopLevelDeclaration : Writable

class KotlinFile(
    var packageName: String?,
    imports: List<Import> = listOf(),
    members: List<TopLevelDeclaration> = listOf(),
    block: KotlinFile.() -> Unit = {},
) : Writable {
    val imports: MutableList<Import> = imports.toMutableList()
    val members: MutableList<TopLevelDeclaration> = members.toMutableList()

    init {
        block()
    }

    operator fun Import.unaryPlus() = apply { imports += this }
    operator fun TopLevelDeclaration.unaryPlus() = apply { members += this }

    override fun write(writer: CodeWriter) {
        packageName?.let {
            writer.write("package ${it.safePackage}")
            writer.newLine()
            writer.newLine()
        }

        if (imports.isNotEmpty()) {
            imports.sortedBy { it.packagePath }.distinctBy { it.packagePath }.forEach { import ->
                import.write(writer)
                writer.newLine()
            }
            writer.newLine()
        }

        members.forEach {
            it.write(writer)
            writer.newLine(LINE_SEPARATOR)
        }
    }

    override fun render() = super.render().trim() + LINE_SEPARATOR
}
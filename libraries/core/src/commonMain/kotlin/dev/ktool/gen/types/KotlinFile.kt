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
) : Writable, SpecificProperties {
    constructor(packageName: String? = null, block: KotlinFile.() -> Unit) : this(packageName) {
        block()
    }

    val imports: MutableList<Import> = imports.toMutableList()
    val members: MutableList<TopLevelDeclaration> = members.toMutableList()

    fun import(packagePath: String, alias: String? = null) {
        imports += Import(packagePath, alias)
    }

    fun clas(name: String, primaryConstructor: PrimaryConstructor? = null, block: Class.() -> Unit) {
        members += Class(name, primaryConstructor, block)
    }

    fun obj(name: String, block: Object.() -> Unit) {
        members += Object(name, block)
    }

    fun inter(name: String, block: Interface.() -> Unit) {
        members += Interface(name, block)
    }

    fun typeAlias(name: String, type: Type, block: TypeAlias.() -> Unit) {
        members += TypeAlias(name, type)
    }

    fun function(name: String, receiver: Type? = null, returnType: Type? = null, block: Function.() -> Unit) {
        members += Function(name, receiver, returnType, block)
    }

    override fun property(
        name: String,
        mutability: Mutability,
        type: Type?,
        initializer: ExpressionBody?,
        block: Property.() -> Unit
    ) {
        members += Property(name, mutability, type, initializer, block)
    }

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
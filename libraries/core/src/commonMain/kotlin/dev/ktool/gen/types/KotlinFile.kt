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

    fun addImport(packagePath: String, alias: String? = null) {
        imports += Import(packagePath, alias)
    }

    fun addClass(name: String, primaryConstructor: PrimaryConstructor? = null, block: Class.() -> Unit) {
        members += Class(name, primaryConstructor, block)
    }

    fun addObject(name: String, block: Object.() -> Unit) {
        members += Object(name, block)
    }

    fun addInterface(name: String, block: Interface.() -> Unit) {
        members += Interface(name, block)
    }

    fun addTypeAlias(name: String, type: Type, block: TypeAlias.() -> Unit) {
        members += TypeAlias(name, type)
    }

    fun addFunction(name: String, receiver: Type? = null, returnType: Type? = null, block: Function.() -> Unit) {
        members += Function(name, receiver, returnType, block)
    }

    fun addLiteral(code: String) {
        members += Literal(code)
    }

    override fun addValProperty(
        name: String,
        type: Type?,
        initializer: ExpressionBody?,
        block: Property.() -> Unit
    ) {
        members += Property(name, Mutability.Val, type, initializer, block)
    }

    override fun addVarProperty(
        name: String,
        type: Type?,
        initializer: ExpressionBody?,
        block: Property.() -> Unit
    ) {
        members += Property(name, Mutability.Var, type, initializer, block)
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
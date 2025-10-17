package dev.ktool.gen.types

import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class FunctionSpec : BddSpec({
    "simple function with no parameters" {
        Given
        val func = Function("doSomething")

        When
        val output = func.render()

        Then
        output shouldBe "fun doSomething()"
    }

    "function with single parameter" {
        Given
        val func = Function(
            name = "greet",
            parameters = listOf(Parameter("name", StringType))
        )

        When
        val output = func.render()

        Then
        output shouldBe "fun greet(name: String)"
    }

    "function with multiple parameters" {
        Given
        val func = Function(
            name = "add",
            parameters = listOf(
                Parameter("a", IntType),
                Parameter("b", IntType)
            )
        )

        When
        val output = func.render()

        Then
        output shouldBe "fun add(a: Int, b: Int)"
    }

    "function with return type" {
        Given
        val func = Function(
            name = "calculate",
            returnType = Type("Double")
        )

        When
        val output = func.render()

        Then
        output shouldBe "fun calculate(): Double"
    }

    "function with parameters and return type" {
        Given
        val func = Function(
            name = "multiply",
            parameters = listOf(
                Parameter("x", IntType),
                Parameter("y", IntType)
            ),
            returnType = IntType
        )

        When
        val output = func.render()

        Then
        output shouldBe "fun multiply(x: Int, y: Int): Int"
    }

    "function with expression body" {
        Given
        val func = Function(
            name = "square",
            parameters = listOf(Parameter("n", IntType)),
            returnType = IntType,
            body = ExpressionBody("n * n")
        )

        When
        val output = func.render()

        Then
        output shouldBe "fun square(n: Int): Int = n * n"
    }

    "function with block body" {
        Given
        val func = Function(
            name = "printMessage",
            parameters = listOf(Parameter("msg", StringType)),
            body = FunctionBlock("println(msg)")
        )

        When
        val output = func.render()

        Then
        output shouldBe """
            fun printMessage(msg: String) {
                println(msg)
            }
        """.trimIndent()
    }

    "function with multiple statements in block body" {
        Given
        val func = Function(
            name = "process",
            body = FunctionBlock(
                "val result = compute()",
                "validate(result)",
                "return result"
            )
        )

        When
        val output = func.render()

        Then
        output shouldBe """
            fun process() {
                val result = compute()
                validate(result)
                return result
            }
        """.trimIndent()
    }

    "function with modifier" {
        Given
        val func = Function("execute") {
            +Modifier.Private
        }

        When
        val output = func.render()

        Then
        output shouldBe "private fun execute()"
    }

    "function with multiple modifiers" {
        Given
        val func = Function("doWork") {
            +listOf(Modifier.Override, Modifier.Suspend)
        }

        When
        val output = func.render()

        Then
        output shouldBe "override suspend fun doWork()"
    }

    "function with single type parameter" {
        Given
        val func = Function("identity") {
            +TypeParameter("T")
            +Parameter("value", Type("T"))
            returnType = Type("T")
        }

        When
        val output = func.render()

        Then
        output shouldBe "fun <T> identity(value: T): T"
    }

    "function with multiple type parameters" {
        Given
        val func = Function("pair") {
            +TypeParameter("A")
            +TypeParameter("B")
            +Parameter("first", Type("A"))
            +Parameter("second", Type("B"))
            returnType = Type("Pair") {
                +TypeArgument("A")
                +TypeArgument("B")
            }
        }

        When
        val output = func.render()

        Then
        output shouldBe "fun <A, B> pair(first: A, second: B): Pair<A, B>"
    }

    "extension function with receiver" {
        Given
        val func = Function(
            name = "isEven",
            receiver = IntType,
            returnType = Type("Boolean"),
            body = ExpressionBody("this % 2 == 0")
        )

        When
        val output = func.render()

        Then
        output shouldBe "fun Int.isEven(): Boolean = this % 2 == 0"
    }

    "extension function with receiver and parameters" {
        Given
        val func = Function(
            name = "append",
            receiver = StringType,
            parameters = listOf(Parameter("suffix", StringType)),
            returnType = StringType,
            body = ExpressionBody("this + suffix")
        )

        When
        val output = func.render()

        Then
        output shouldBe "fun String.append(suffix: String): String = this + suffix"
    }

    "function with parameter default value" {
        Given
        val func = Function(
            name = "greet",
            parameters = listOf(
                Parameter("name", StringType),
                Parameter("greeting", StringType, defaultValue = ExpressionBody("\"Hello\""))
            ),
            returnType = StringType
        )

        When
        val output = func.render()

        Then
        output shouldBe "fun greet(name: String, greeting: String = \"Hello\"): String"
    }

    "function with keyword name gets backticks" {
        Given
        val func = Function("class")

        When
        val output = func.render()

        Then
        output shouldBe "fun `class`()"
    }

    "inline function" {
        Given
        val func = Function(
            name = "repeat",
            modifiers = listOf(Modifier.Inline),
            parameters = listOf(
                Parameter("times", IntType),
                Parameter("action", Type("() -> Unit"))
            ),
            body = FunctionBlock("for (i in 0 until times) action()")
        )

        When
        val output = func.render()

        Then
        output shouldBe """
            inline fun repeat(times: Int, action: () -> Unit) {
                for (i in 0 until times) action()
            }
        """.trimIndent()
    }

    "operator function" {
        Given
        val func = Function(
            name = "plus",
            modifiers = listOf(Modifier.Operator),
            parameters = listOf(Parameter("other", Type("MyClass"))),
            returnType = Type("MyClass"),
            body = ExpressionBody("MyClass(this.value + other.value)")
        )

        When
        val output = func.render()

        Then
        output shouldBe "operator fun plus(other: MyClass): MyClass = MyClass(this.value + other.value)"
    }

    "infix function" {
        Given
        val func = Function(
            name = "to",
            modifiers = listOf(Modifier.Infix),
            receiver = Type("A"),
            typeParameters = listOf(TypeParameter("A"), TypeParameter("B")),
            parameters = listOf(Parameter("that", Type("B"))),
            returnType = Type("Pair<A, B>"),
            body = ExpressionBody("Pair(this, that)")
        )

        When
        val output = func.render()

        Then
        output shouldBe "infix fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)"
    }

    "abstract function" {
        Given
        val func = Function(
            name = "execute",
            modifiers = listOf(Modifier.Abstract),
            returnType = Type("Unit")
        )

        When
        val output = func.render()

        Then
        output shouldBe "abstract fun execute(): Unit"
    }

    "modifying function name" {
        Given
        val func = Function("oldName")
        func.name = "newName"

        When
        val output = func.render()

        Then
        output shouldBe "fun newName()"
    }

    "modifying parameters" {
        Given
        val func = Function("process") {
            +Parameter("data", StringType)
        }

        When
        val output = func.render()

        Then
        output shouldBe "fun process(data: String)"
    }

    "modifying return type" {
        Given
        val func = Function("compute")
        func.returnType = IntType

        When
        val output = func.render()

        Then
        output shouldBe "fun compute(): Int"
    }

    "complex function with all features" {
        Given
        val func = Function(
            name = "transform",
            receiver = Type("List<T>"),
            modifiers = listOf(Modifier.Inline),
            typeParameters = listOf(TypeParameter("T"), TypeParameter("R")),
            parameters = listOf(
                Parameter("transform", Type("(T) -> R"))
            ),
            returnType = Type("List<R>"),
            body = ExpressionBody("map(transform)")
        )

        When
        val output = func.render()

        Then
        output shouldBe "inline fun <T, R> List<T>.transform(transform: (T) -> R): List<R> = map(transform)"
    }

    "function with multiline body" {
        Given
        val func = Function(name = "transform").apply {
            body = FunctionBlock(
                "val a = \"blah\"",
                """
                if(a != "blah") {
                    println(a)
                else {
                    println("something else")
                }
                println("bye")
            """.trimIndent()
            )
        }

        When
        val output = func.render()

        Then
        output shouldBe """
            fun transform() {
                val a = "blah"
                if(a != "blah") {
                    println(a)
                else {
                    println("something else")
                }
                println("bye")
            }
        """.trimIndent()
    }
})

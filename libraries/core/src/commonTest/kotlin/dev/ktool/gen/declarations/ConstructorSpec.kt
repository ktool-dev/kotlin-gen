package dev.ktool.gen.declarations

import dev.ktool.gen.types.IntType
import dev.ktool.gen.types.Modifier
import dev.ktool.gen.types.Parameter
import dev.ktool.gen.types.StringType
import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class ConstructorSpec : BddSpec({
    "constructor with no parameters or body" {
        Given
        val constructor = Constructor()

        When
        val output = constructor.render()

        Then
        output shouldBe "constructor()"
    }

    "constructor with single parameter" {
        Given
        val constructor = Constructor()
        constructor.parameters.add(Parameter("name", StringType))

        When
        val output = constructor.render()

        Then
        output shouldBe "constructor(name: String)"
    }

    "constructor with multiple parameters" {
        Given
        val constructor = Constructor()
        constructor.parameters.add(Parameter("x", IntType))
        constructor.parameters.add(Parameter("y", IntType))

        When
        val output = constructor.render()

        Then
        output shouldBe "constructor(x: Int, y: Int)"
    }

    "constructor with modifiers" {
        Given
        val constructor = Constructor()
        constructor.modifiers.add(Modifier.PRIVATE)
        constructor.parameters.add(Parameter("id", IntType))

        When
        val output = constructor.render()

        Then
        output shouldBe "private constructor(id: Int)"
    }

    "constructor with body" {
        Given
        val constructor = Constructor(body = Block(mutableListOf("println(\"constructed\")")))

        When
        val output = constructor.render()

        Then
        output shouldBe """
            constructor() {
                println("constructed")
            }
        """.trimIndent()
    }

    "constructor with parameters and body" {
        Given
        val block = Block().apply {
            statements.add("this.name = name")
        }
        val constructor = Constructor(body = block).apply {
            parameters.add(Parameter("name", StringType))
        }

        When
        val output = constructor.render()

        Then
        output shouldBe """
            constructor(name: String) {
                this.name = name
            }
        """.trimIndent()
    }
})
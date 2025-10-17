package dev.ktool.gen.types

import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class ConstructorSpec : BddSpec({
    "constructor with no parameters or body" {
        Given
        val constructor = Constructor()

        When
        val output = constructor.render()

        Then
        output shouldBe "constructor() : this()"
    }

    "constructor with single parameter" {
        Given
        val constructor = Constructor {
            +Parameter("name", StringType)
        }

        When
        val output = constructor.render()

        Then
        output shouldBe "constructor(name: String) : this()"
    }

    "constructor with multiple parameters" {
        Given
        val constructor = Constructor {
            +Parameter("x", IntType)
            +Parameter("y", IntType)
        }

        When
        val output = constructor.render()

        Then
        output shouldBe "constructor(x: Int, y: Int) : this()"
    }

    "constructor with modifiers" {
        Given
        val constructor = Constructor {
            +Modifier.Private
            +Parameter("id", IntType)
        }

        When
        val output = constructor.render()

        Then
        output shouldBe "private constructor(id: Int) : this()"
    }

    "constructor with body" {
        Given
        val constructor = Constructor(body = Block("println(\"constructed\")"))

        When
        val output = constructor.render()

        Then
        output shouldBe """
            constructor() : this() {
                println("constructed")
            }
        """.trimIndent()
    }

    "constructor with parameters and body" {
        Given
        val block = Block("this.name = name")
        val constructor = Constructor(body = block).apply {
            +Parameter("name", StringType)
        }

        When
        val output = constructor.render()

        Then
        output shouldBe """
            constructor(name: String) : this() {
                this.name = name
            }
        """.trimIndent()
    }
})
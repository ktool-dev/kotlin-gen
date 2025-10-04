package dev.ktool.gen.types

import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class InitBlockSpec : BddSpec({
    "init block with single statement" {
        Given
        val initBlock = InitBlock("println(\"initialized\")")

        When
        val output = initBlock.render()

        Then
        output shouldBe """
            init {
                println("initialized")
            }
        """.trimIndent()
    }

    "init block with multiple statements" {
        Given
        val initBlock = InitBlock("get x = 10", "println(x)")

        When
        val output = initBlock.render()

        Then
        output shouldBe """
            init {
                get x = 10
                println(x)
            }
        """.trimIndent()
    }

    "empty init block" {
        Given
        val initBlock = InitBlock()

        When
        val output = initBlock.render()

        Then
        output shouldBe """
            init {
            }
        """.trimIndent()
    }
})
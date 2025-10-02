package dev.ktool.gen.declarations

import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class InitBlockSpec : BddSpec({
    "init block with single statement" {
        Given
        val initBlock = InitBlock()
        initBlock.statements.add("println(\"initialized\")")

        When
        val output = initBlock.render()

        Then
        output shouldBe """init {
    println("initialized")
}"""
    }

    "init block with multiple statements" {
        Given
        val initBlock = InitBlock()
        initBlock.statements.add("get x = 10")
        initBlock.statements.add("println(x)")

        When
        val output = initBlock.render()

        Then
        output shouldBe """init {
    get x = 10
    println(x)
}"""
    }

    "empty init block" {
        Given
        val initBlock = InitBlock()

        When
        val output = initBlock.render()

        Then
        output shouldBe """init {
}"""
    }
})
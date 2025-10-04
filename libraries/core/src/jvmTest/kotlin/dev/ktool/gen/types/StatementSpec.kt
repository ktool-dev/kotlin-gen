package dev.ktool.gen.types

import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class StatementSpec : BddSpec({
    "Literal with simple string" {
        Given
        val literal = Literal("val x = 10")

        When
        val output = literal.render()

        Then
        output shouldBe "val x = 10\n"
    }

    "Literal with multiline string" {
        Given
        val literal = Literal("if (condition) {\n    doSomething()\n}")

        When
        val output = literal.render()

        Then
        output shouldBe "if (condition) {\n    doSomething()\n}\n"
    }

    "String toStatement extension" {
        Given
        val statement = "println(\"hello\")".toStatement()

        When
        val output = statement.render()

        Then
        output shouldBe "println(\"hello\")\n"
    }

    "FunctionCall with name only" {
        Given
        val call = FunctionCall("doSomething")

        When
        val output = call.render()

        Then
        output shouldBe "doSomething()\n"
    }

    "FunctionCall with single argument" {
        Given
        val call = FunctionCall("println", listOf("\"hello\""))

        When
        val output = call.render()

        Then
        output shouldBe "println(\"hello\")\n"
    }

    "FunctionCall with multiple arguments" {
        Given
        val call = FunctionCall("add", listOf("1", "2", "3"))

        When
        val output = call.render()

        Then
        output shouldBe "add(1, 2, 3)\n"
    }

    "FunctionCall with target" {
        Given
        val call = FunctionCall("println", listOf("\"message\""), target = "console")

        When
        val output = call.render()

        Then
        output shouldBe "console.println(\"message\")\n"
    }

    "FunctionCall with target and multiple arguments" {
        Given
        val call = FunctionCall("substring", listOf("0", "5"), target = "text")

        When
        val output = call.render()

        Then
        output shouldBe "text.substring(0, 5)\n"
    }

    "FunctionCall with Block as last argument" {
        Given
        val block = Block("println(\"inside\")")
        val call = FunctionCall("run", listOf(block))

        When
        val output = call.render()

        Then
        output shouldBe """
            run {
                println("inside")
            }
            
        """.trimIndent()
    }

    "FunctionCall with arguments and Block as last argument" {
        Given
        val block = Block("println(it)")
        val call = FunctionCall("repeat", listOf("3", block))

        When
        val output = call.render()

        Then
        output shouldBe """
            repeat(3) {
                println(it)
            }
            
        """.trimIndent()
    }

    "FunctionCall with target and Block as last argument" {
        Given
        val block = Block("add(item)")
        val call = FunctionCall("forEach", listOf(block), target = "list")

        When
        val output = call.render()

        Then
        output shouldBe """
            list.forEach {
                add(item)
            }
            
        """.trimIndent()
    }

    "FunctionCall with target, arguments and Block" {
        Given
        val block = Block("println(it)")
        val call = FunctionCall("map", listOf("transform", block), target = "list")

        When
        val output = call.render()

        Then
        output shouldBe """
            list.map(transform) {
                println(it)
            }
            
        """.trimIndent()
    }

    "FunctionCall toString uses render" {
        Given
        val call = FunctionCall("test", listOf("arg"))

        When
        val output = call.toString()

        Then
        output shouldBe "test(arg)\n"
    }

    "Literal toString uses render" {
        Given
        val literal = Literal("test value")

        When
        val output = literal.toString()

        Then
        output shouldBe "test value\n"
    }
})

package dev.ktool.gen.types

import dev.ktool.gen.declarations.ExpressionBody
import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class ParameterSpec : BddSpec({
    "simple parameter" {
        Given
        val param = Parameter("name", StringType)

        When
        val output = param.render()

        Then
        output shouldBe "name: String"
    }

    "parameter with nullable type" {
        Given
        val param = Parameter("value", Type("Int", nullable = true))

        When
        val output = param.render()

        Then
        output shouldBe "value: Int?"
    }

    "parameter with default value" {
        Given
        val param = Parameter("count", IntType)
        param.defaultValue = ExpressionBody("0")

        When
        val output = param.render()

        Then
        output shouldBe "count: Int = 0"
    }

    "parameter with vararg modifier" {
        Given
        val param = Parameter("items", StringType)
        param.modifiers.add(Modifier.VARARG)

        When
        val output = param.render()

        Then
        output shouldBe "vararg items: String"
    }

    "parameter with crossinline modifier" {
        Given
        val param = Parameter("block", Type("Function"))
        param.modifiers.add(Modifier.CROSSINLINE)

        When
        val output = param.render()

        Then
        output shouldBe "crossinline block: Function"
    }

    "parameter with keyword name gets backticks" {
        Given
        val param = Parameter("class", StringType)

        When
        val output = param.render()

        Then
        output shouldBe "`class`: String"
    }

    "parameter with generic type" {
        Given
        val listType = Type("List")
        listType.typeArguments.add(StringType)
        val param = Parameter("items", listType)

        When
        val output = param.render()

        Then
        output shouldBe "items: List<String>"
    }
})
package dev.ktool.gen.declarations

import dev.ktool.gen.types.IntType
import dev.ktool.gen.types.Modifier
import dev.ktool.gen.types.Parameter
import dev.ktool.gen.types.StringType
import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class PrimaryConstructorSpec : BddSpec({
    "primary constructor with no parameters" {
        Given
        val constructor = PrimaryConstructor()

        When
        val output = constructor.render()

        Then
        output shouldBe ""
    }

    "primary constructor with single parameter" {
        Given
        val constructor = PrimaryConstructor()
        constructor.parameters.add(Parameter("name", StringType))

        When
        val output = constructor.render()

        Then
        output shouldBe "(name: String)"
    }

    "primary constructor with multiple parameters" {
        Given
        val constructor = PrimaryConstructor()
        constructor.parameters.add(Parameter("name", StringType))
        constructor.parameters.add(Parameter("age", IntType))

        When
        val output = constructor.render()

        Then
        output shouldBe "(name: String, age: Int)"
    }

    "primary constructor with modifiers" {
        Given
        val constructor = PrimaryConstructor()
        constructor.modifiers.add(Modifier.PRIVATE)
        constructor.parameters.add(Parameter("id", IntType))

        When
        val output = constructor.render()

        Then
        output shouldBe " private constructor(id: Int)"
    }

    "primary constructor with internal modifier" {
        Given
        val constructor = PrimaryConstructor()
        constructor.modifiers.add(Modifier.INTERNAL)

        When
        val output = constructor.render()

        Then
        output shouldBe " internal constructor()"
    }
})
package dev.ktool.gen.types

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
        val constructor = PrimaryConstructor {
            +Parameter("name", StringType)
        }

        When
        val output = constructor.render()

        Then
        output shouldBe "(name: String)"
    }

    "primary constructor with multiple parameters" {
        Given
        val constructor = PrimaryConstructor {
            +Parameter("name", StringType)
            +Parameter("age", IntType)
        }

        When
        val output = constructor.render()

        Then
        output shouldBe "(name: String, age: Int)"
    }

    "primary constructor with modifiers" {
        Given
        val constructor = PrimaryConstructor {
            +Modifier.Private
            +Parameter("id", IntType)
        }

        When
        val output = constructor.render()

        Then
        output shouldBe " private constructor(id: Int)"
    }

    "primary constructor with internal modifier" {
        Given
        val constructor = PrimaryConstructor {
            +Modifier.Internal
        }

        When
        val output = constructor.render()

        Then
        output shouldBe " internal constructor()"
    }
})
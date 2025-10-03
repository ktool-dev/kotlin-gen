package dev.ktool.gen.types

import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class ObjectSpec : BddSpec({
    "simple object" {
        Given
        val obj = Object("Singleton")

        When
        val output = obj.render()

        Then
        output shouldBe "object Singleton"
    }

    "object with modifier" {
        Given
        val obj = Object("Config")
        obj.modifiers.add(Modifier.Internal)

        When
        val output = obj.render()

        Then
        output shouldBe "internal object Config"
    }

    "object with single supertype" {
        Given
        val obj = Object("Counter")
        obj.superTypes.add(Type("Countable"))

        When
        val output = obj.render()

        Then
        output shouldBe "object Counter : Countable"
    }

    "object with multiple supertypes" {
        Given
        val obj = Object("Logger")
        obj.superTypes.add(Type("Loggable"))
        obj.superTypes.add(Type("AutoCloseable"))

        When
        val output = obj.render()

        Then
        output shouldBe "object Logger : Loggable, AutoCloseable"
    }

    "object with property member" {
        Given
        val obj = Object("Constants")
        obj.members.add(Property("PI", type = Type("Double")))

        When
        val output = obj.render()

        Then
        output shouldBe """
            object Constants {
                val PI: Double
            }
        """.trimIndent()
    }

    "object with function member" {
        Given
        val obj = Object("Utils")
        obj.members.add(Function("log"))

        When
        val output = obj.render()

        Then
        output shouldBe """
            object Utils {
                fun log()
            }
        """.trimIndent()
    }

    "object with multiple members" {
        Given
        val obj = Object("AppConfig")
        obj.members.add(Property("version", type = StringType))
        obj.members.add(Function("initialize"))

        When
        val output = obj.render()

        Then
        output shouldBe """
            object AppConfig {
                val version: String
            
                fun initialize()
            }
        """.trimIndent()
    }

    "companion object" {
        Given
        val obj = Object("Companion")
        obj.members.add(Function("create"))

        When
        val output = obj.render()

        Then
        output shouldBe """
            object Companion {
                fun create()
            }
        """.trimIndent()
    }
})
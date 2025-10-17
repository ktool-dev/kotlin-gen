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
        val obj = Object("Config", listOf(Modifier.Internal))

        When
        val output = obj.render()

        Then
        output shouldBe "internal object Config"
    }

    "object with single supertype" {
        Given
        val obj = Object("Counter") {
            +SuperType("Countable")
        }

        When
        val output = obj.render()

        Then
        output shouldBe "object Counter : Countable"
    }

    "object with multiple supertypes" {
        Given
        val obj = Object("Logger") {
            +SuperType("Loggable")
            +SuperType("AutoCloseable")
        }

        When
        val output = obj.render()

        Then
        output shouldBe "object Logger : Loggable, AutoCloseable"
    }

    "object with property member" {
        Given
        val obj = Object("Constants") {
            +Property("PI", type = Type("Double"))
        }

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
        val obj = Object("Utils") {
            +Function("log")
        }

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
        val obj = Object("AppConfig") {
            +Property("version", type = StringType)
            +Function("initialize")
        }

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
        val obj = Object("Companion") {
            +Function("create")
        }

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
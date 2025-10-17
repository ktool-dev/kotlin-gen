package dev.ktool.gen.types

import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class InterfaceSpec : BddSpec({
    "simple interface" {
        Given
        val iFace = Interface("Drawable")

        When
        val output = iFace.render()

        Then
        output shouldBe "interface Drawable"
    }

    "interface with modifier" {
        Given
        val iFace = Interface("Internal", listOf(Modifier.Internal))

        When
        val output = iFace.render()

        Then
        output shouldBe "internal interface Internal"
    }

    "interface with type parameter" {
        Given
        val iFace = Interface("Container") {
            +TypeParameter("T")
        }

        When
        val output = iFace.render()

        Then
        output shouldBe "interface Container<T>"
    }

    "interface with multiple type parameters" {
        Given
        val iFace = Interface("Mapper") {
            +TypeParameter("K")
            +TypeParameter("V")
        }

        When
        val output = iFace.render()

        Then
        output shouldBe "interface Mapper<K, V>"
    }

    "interface with single supertype" {
        Given
        val iFace = Interface("MutableList") {
            +SuperType("List")
        }

        When
        val output = iFace.render()

        Then
        output shouldBe "interface MutableList : List"
    }

    "interface with multiple supertypes" {
        Given
        val iFace = Interface("Serializable") {
            +SuperType("Readable")
            +SuperType("Writable")
        }

        When
        val output = iFace.render()

        Then
        output shouldBe "interface Serializable : Readable, Writable"
    }

    "interface with function member" {
        Given
        val iFace = Interface("Clickable") {
            +Function("onClick")
        }

        When
        val output = iFace.render()

        Then
        output shouldBe """
            interface Clickable {
                fun onClick()
            }
        """.trimIndent()
    }

    "interface with property member" {
        Given
        val iFace = Interface("Named") {
            +Property("name", type = StringType)
        }

        When
        val output = iFace.render()

        Then
        output shouldBe """
            interface Named {
                val name: String
            }
        """.trimIndent()
    }

    "interface with all features" {
        Given
        val iFace = Interface("Repository") {
            +TypeParameter("T")
            +SuperType("AutoCloseable")
            +Function("save")
            +Property("count", type = IntType)
        }

        When
        val output = iFace.render()

        Then
        output shouldBe """
            interface Repository<T> : AutoCloseable {
                fun save()
            
                val count: Int
            }
        """.trimIndent()
    }
})
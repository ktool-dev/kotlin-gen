package dev.ktool.gen.types

import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class TypeAliasSpec : BddSpec({
    "simple typealias" {
        Given
        val typeAlias = TypeAlias("StringList", Type("List") {
            +TypeArgument("String")
        })

        When
        val output = typeAlias.render()

        Then
        output shouldBe "typealias StringList = List<String>"
    }

    "typealias to simple type" {
        Given
        val typeAlias = TypeAlias("MyString", StringType)

        When
        val output = typeAlias.render()

        Then
        output shouldBe "typealias MyString = String"
    }

    "typealias to complex type" {
        Given
        val mapType = Type("Map") {
            +TypeArgument("String")
            +TypeArgument("List") {
                +TypeArgument("Int")
            }
        }
        val typeAlias = TypeAlias("StringToIntList", mapType)

        When
        val output = typeAlias.render()

        Then
        output shouldBe "typealias StringToIntList = Map<String, List<Int>>"
    }

    "typealias with modifiers" {
        Given
        val typeAlias = TypeAlias("Internal", StringType) {
            +Modifier.Internal
        }

        When
        val output = typeAlias.render()

        Then
        output shouldBe "internal typealias Internal = String"
    }

    "typealias with type parameters" {
        Given
        val typeAlias = TypeAlias(
            "Predicate",
            Type("Function1") {
                +TypeArgument("T")
                +TypeArgument("Boolean")
            }
        ) {
            +TypeParameter("T")
        }

        When
        val output = typeAlias.render()

        Then
        output shouldBe "typealias Predicate<T> = Function1<T, Boolean>"
    }

    "typealias with multiple type parameters" {
        Given
        val typeAlias = TypeAlias(
            "BiFunction",
            Type("Function2") {
                +TypeArgument("A")
                +TypeArgument("B")
                +TypeArgument("R")
            }
        ) {
            +TypeParameter("A")
            +TypeParameter("B")
            +TypeParameter("R")
        }

        When
        val output = typeAlias.render()

        Then
        output shouldBe "typealias BiFunction<A, B, R> = Function2<A, B, R>"
    }

    "typealias with bounded type parameter" {
        Given
        val typeAlias = TypeAlias(
            "ComparableList",
            Type("List") {
                +TypeArgument("T")
            }
        ) {
            +TypeParameter("T") {
                +TypeArgument("Comparable") {
                    +TypeArgument("T")
                }
            }
        }

        When
        val output = typeAlias.render()

        Then
        output shouldBe "typealias ComparableList<T : Comparable<T>> = List<T>"
    }

    "typealias with variance in type parameter" {
        Given
        val typeAlias = TypeAlias(
            "Producer",
            Type("Function0") {
                +TypeArgument("T")
            }
        ) {
            +TypeParameter("T", variance = Variance.Out)
        }

        When
        val output = typeAlias.render()

        Then
        output shouldBe "typealias Producer<out T> = Function0<T>"
    }

    "typealias to nullable type" {
        Given
        val typeAlias = TypeAlias("NullableString", Type("String", nullable = true))

        When
        val output = typeAlias.render()

        Then
        output shouldBe "typealias NullableString = String?"
    }

    "typealias with keyword name gets backticks" {
        Given
        val typeAlias = TypeAlias("class", StringType)

        When
        val output = typeAlias.render()

        Then
        output shouldBe "typealias `class` = String"
    }

    "modifying typealias name" {
        Given
        val typeAlias = TypeAlias("Original", StringType)
        typeAlias.name = "Modified"

        When
        val output = typeAlias.render()

        Then
        output shouldBe "typealias Modified = String"
    }

    "modifying typealias type" {
        Given
        val typeAlias = TypeAlias("MyType", StringType)
        typeAlias.type = IntType

        When
        val output = typeAlias.render()

        Then
        output shouldBe "typealias MyType = Int"
    }
})

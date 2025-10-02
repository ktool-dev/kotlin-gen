package dev.ktool.gen.types

import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class TypeSpec : BddSpec({
    "simple type" {
        Given
        val type = StringType

        When
        val output = type.render()

        Then
        output shouldBe "String"
    }

    "nullable type" {
        Given
        val type = Type("String", nullable = true)

        When
        val output = type.render()

        Then
        output shouldBe "String?"
    }

    "type with single type argument" {
        Given
        val type = Type("List")
        type.typeArguments.add(StringType)

        When
        val output = type.render()

        Then
        output shouldBe "List<String>"
    }

    "type with multiple type arguments" {
        Given
        val type = Type("Map")
        type.typeArguments.add(StringType)
        type.typeArguments.add(IntType)

        When
        val output = type.render()

        Then
        output shouldBe "Map<String, Int>"
    }

    "nullable type with type arguments" {
        Given
        val type = Type("List", nullable = true)
        type.typeArguments.add(StringType)

        When
        val output = type.render()

        Then
        output shouldBe "List<String>?"
    }

    "nested type arguments" {
        Given
        val innerType = StringType
        val listType = Type("List")
        listType.typeArguments.add(innerType)
        val outerType = Type("Set")
        outerType.typeArguments.add(listType)

        When
        val output = outerType.render()

        Then
        output shouldBe "Set<List<String>>"
    }

    "modifying type name" {
        Given
        val type = Type("MyType")
        type.name = "Other"

        When
        val output = type.render()

        Then
        output shouldBe "Other"
    }

    "modifying nullable" {
        Given
        val type = Type("MyType")
        type.nullable = true

        When
        val output = type.render()

        Then
        output shouldBe "MyType?"
    }
})
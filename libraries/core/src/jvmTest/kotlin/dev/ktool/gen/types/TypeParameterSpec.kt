package dev.ktool.gen.types

import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class TypeParameterSpec : BddSpec({
    "simple type parameter" {
        Given
        val typeParam = TypeParameter("T")

        When
        val output = typeParam.render()

        Then
        output shouldBe "T"
    }

    "type parameter with single bound" {
        Given
        val typeParam = TypeParameter("T")
        typeParam.typeArguments.add(Type("Number"))

        When
        val output = typeParam.render()

        Then
        output shouldBe "T : Number"
    }

    "type parameter with multiple bounds" {
        Given
        val typeParam = TypeParameter("T")
        typeParam.typeArguments.add(Type("Comparable"))
        typeParam.typeArguments.add(Type("Serializable"))

        When
        val output = typeParam.render()

        Then
        output shouldBe "T : Comparable, Serializable"
    }

    "type parameter with out variance" {
        Given
        val typeParam = TypeParameter("T", variance = Variance.Out)

        When
        val output = typeParam.render()

        Then
        output shouldBe "out T"
    }

    "type parameter with in variance" {
        Given
        val typeParam = TypeParameter("T", variance = Variance.In)

        When
        val output = typeParam.render()

        Then
        output shouldBe "in T"
    }

    "type parameter with variance and bound" {
        Given
        val typeParam = TypeParameter("T", variance = Variance.Out)
        typeParam.typeArguments.add(Type("Any"))

        When
        val output = typeParam.render()

        Then
        output shouldBe "out T : Any"
    }

    "type parameter with keyword name gets backticks" {
        Given
        val typeParam = TypeParameter("class")

        When
        val output = typeParam.render()

        Then
        output shouldBe "`class`"
    }
})
package dev.ktool.gen.declarations

import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class KotlinFileSpec : BddSpec({
    "file with package only" {
        Given
        val file = KotlinFile(packageName = "com.example")

        When
        val output = file.render()

        Then
        output shouldBe "package com.example\n"
    }

    "file without package" {
        Given
        val file = KotlinFile(packageName = null)

        When
        val output = file.render()

        Then
        output shouldBe "\n"
    }

    "file with package and single import" {
        Given
        val file = KotlinFile(
            packageName = "com.example",
            imports = listOf(Import("kotlin.collections.List"))
        )

        When
        val output = file.render()

        Then
        output shouldBe """
            package com.example

            import kotlin.collections.List

        """.trimIndent()
    }

    "file with package and multiple imports" {
        Given
        val file = KotlinFile(
            packageName = "com.example",
            imports = listOf(
                Import("kotlin.collections.List"),
                Import("kotlin.collections.Map")
            )
        )

        When
        val output = file.render()

        Then
        output shouldBe """
            package com.example

            import kotlin.collections.List
            import kotlin.collections.Map

        """.trimIndent()
    }

    "file with get containing keyword gets backticks" {
        Given
        val file = KotlinFile(packageName = "com.class.example")

        When
        val output = file.render()

        Then
        output shouldBe "package com.`class`.example\n"
    }

    "file with top level declaration" {
        Given
        val file = KotlinFile(
            packageName = "com.example",
            topLevelDeclarations = listOf(Class("MyClass"))
        )

        When
        val output = file.render()

        Then
        output shouldBe """
            package com.example

            class MyClass
            
        """.trimIndent()
    }

    "file with multiple top level declarations" {
        Given
        val file = KotlinFile(
            packageName = "com.example",
            topLevelDeclarations = listOf(Class("MyClass"), Class("AnotherClass"))
        )

        When
        val output = file.render()

        Then
        output shouldBe """
            package com.example

            class MyClass

            class AnotherClass
            
        """.trimIndent()
    }

    "file with imports and top level declarations" {
        Given
        val cls = Class("MyClass")
        val file = KotlinFile(
            packageName = "com.example",
            imports = listOf(Import("kotlin.collections.List")),
            topLevelDeclarations = listOf(cls)
        )

        When
        val output = file.render()

        Then
        output shouldBe """
            package com.example

            import kotlin.collections.List

            class MyClass
            
        """.trimIndent()
    }

    "modifying package name" {
        Given
        val file = KotlinFile(packageName = "com.old")
        file.packageName = "com.new"

        When
        val output = file.render()

        Then
        output shouldBe "package com.new\n"
    }

    "modifying imports" {
        Given
        val file = KotlinFile(
            packageName = "com.example",
            imports = listOf(Import("kotlin.collections.List"))
        )
        file.imports += Import("kotlin.collections.Map")

        When
        val output = file.render()

        Then
        output shouldBe """
            package com.example

            import kotlin.collections.List
            import kotlin.collections.Map

        """.trimIndent()
    }

    "modifying top level declarations" {
        Given
        val file = KotlinFile(
            packageName = "com.example",
            topLevelDeclarations = listOf(Class("FirstClass"))
        )
        file.topLevelDeclarations += Class("SecondClass")

        When
        val output = file.render()

        Then
        output shouldBe """
            package com.example

            class FirstClass

            class SecondClass

        """.trimIndent()
    }
})

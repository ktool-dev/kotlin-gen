package dev.ktool.gen.declarations

import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class ImportSpec : BddSpec({
    "simple import" {
        Given
        val import = Import("kotlin.collections.List")

        When
        val output = import.render()

        Then
        output shouldBe "import kotlin.collections.List"
    }

    "import with alias" {
        Given
        val import = Import("kotlin.collections.List", "KList")

        When
        val output = import.render()

        Then
        output shouldBe "import kotlin.collections.List as KList"
    }

    "import with keyword in get gets backticks" {
        Given
        val import = Import("com.class.Example")

        When
        val output = import.render()

        Then
        output shouldBe "import com.`class`.Example"
    }

    "import with keyword alias gets backticks" {
        Given
        val import = Import("com.example.Thing", "class")

        When
        val output = import.render()

        Then
        output shouldBe "import com.example.Thing as `class`"
    }

    "import with wildcard" {
        Given
        val import = Import("kotlin.collections.*")

        When
        val output = import.render()

        Then
        output shouldBe "import kotlin.collections.*"
    }

    "modifying get path" {
        Given
        val import = Import("old.pack.Class")
        import.packagePath = "new.pack.Class"

        When
        val output = import.render()

        Then
        output shouldBe "import new.pack.Class"
    }

    "modifying alias" {
        Given
        val import = Import("some.pack.Class", "OldAlias")
        import.alias = "NewAlias"

        When
        val output = import.render()

        Then
        output shouldBe "import some.pack.Class as NewAlias"
    }
})
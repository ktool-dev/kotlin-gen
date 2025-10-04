package dev.ktool.gen

import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class CodeWriterSpec : BddSpec({
    "writing code to current line" {
        Given
        val writer = CodeWriter()

        When
        writer.write("hello").write(" world")

        Then
        writer.toString() shouldBe "hello world"
    }

    "writing multiline string splits into multiple lines" {
        Given
        val writer = CodeWriter()

        When
        writer.write("first line${LINE_SEPARATOR}second line${LINE_SEPARATOR}third line")

        Then
        writer.toString() shouldBe "first line${LINE_SEPARATOR}second line${LINE_SEPARATOR}third line${LINE_SEPARATOR}"
    }

    "writing multiline string with indentation" {
        Given
        val writer = CodeWriter()

        When
        writer.indent().write("line 1${LINE_SEPARATOR}line 2${LINE_SEPARATOR}line 3")

        Then
        writer.toString() shouldBe "line 1${LINE_SEPARATOR}    line 2${LINE_SEPARATOR}    line 3${LINE_SEPARATOR}"
    }

    "writing multiline string trims indent from source" {
        Given
        val writer = CodeWriter()

        When
        writer.write(
            """
            get example() {
                println("hello")
            }
        """
        )

        Then
        writer.toString() shouldBe """get example() {
    println("hello")
}
"""
    }

    "new line with default empty content" {
        Given
        val writer = CodeWriter()

        When
        writer.newLine()

        Then
        writer.toString() shouldBe "${LINE_SEPARATOR}"
    }

    "new line with content" {
        Given
        val writer = CodeWriter()

        When
        writer.newLine("first line").newLine("second line")

        Then
        writer.toString() shouldBe "${LINE_SEPARATOR}first line${LINE_SEPARATOR}second line"
    }

    "indentation with default size" {
        Given
        val writer = CodeWriter()

        When
        writer.newLine("no indent")
            .indent()
            .newLine("indented")

        Then
        writer.toString() shouldBe "${LINE_SEPARATOR}no indent${LINE_SEPARATOR}    indented"
    }

    "indentation with custom size" {
        Given
        val writer = CodeWriter(indentationSize = 2)

        When
        writer.newLine("no indent")
            .indent()
            .newLine("indented")

        Then
        writer.toString() shouldBe "${LINE_SEPARATOR}no indent${LINE_SEPARATOR}  indented"
    }

    "multiple indentation levels" {
        Given
        val writer = CodeWriter()

        When
        writer.newLine("level 0")
            .indent()
            .newLine("level 1")
            .indent()
            .newLine("level 2")

        Then
        writer.toString() shouldBe "${LINE_SEPARATOR}level 0${LINE_SEPARATOR}    level 1${LINE_SEPARATOR}        level 2"
    }

    "unindenting reduces indentation level" {
        Given
        val writer = CodeWriter()

        When
        writer.indent()
            .indent()
            .newLine("deeply indented")
            .unindent()
            .newLine("less indented")
            .unindent()
            .newLine("no indent")

        Then
        writer.toString() shouldBe "$LINE_SEPARATOR        deeply indented${LINE_SEPARATOR}    less indented${LINE_SEPARATOR}no indent"
    }

    "withIndent temporarily increases indentation" {
        Given
        val writer = CodeWriter()

        When
        writer.newLine("before")
            .withIndent {
                newLine("inside block")
                withIndent {
                    newLine("nested block")
                }
                newLine("back to first level")
            }
            .newLine("after")

        Then
        writer.toString() shouldBe "${LINE_SEPARATOR}before${LINE_SEPARATOR}    inside block${LINE_SEPARATOR}        nested block${LINE_SEPARATOR}    back to first level${LINE_SEPARATOR}after"
    }

    "removeLastIndentation removes trailing indentation" {
        Given
        val writer = CodeWriter()

        When
        writer.indent()
            .newLine("indented    ")
            .removeLastIndentation()

        Then
        writer.toString() shouldBe "${LINE_SEPARATOR}    indented"
    }

    "removeLastIndentation does nothing when no trailing indentation" {
        Given
        val writer = CodeWriter()

        When
        writer.newLine("no indent")
            .removeLastIndentation()

        Then
        writer.toString() shouldBe "${LINE_SEPARATOR}no indent"
    }

    "trimEnd removes blank last line" {
        Given
        val writer = CodeWriter()

        When
        writer.newLine("content")
            .newLine()
            .trimEnd()

        Then
        writer.toString() shouldBe "${LINE_SEPARATOR}content"
    }

    "trimEnd trims whitespace from last line" {
        Given
        val writer = CodeWriter()

        When
        writer.write("content   ")
            .trimEnd()

        Then
        writer.toString() shouldBe "content"
    }

    "building complex code structure" {
        Given
        val writer = CodeWriter()

        When
        writer.write("class Example {")
            .withIndent {
                newLine("get method() {")
                withIndent {
                    newLine("println(\"Hello World\")")
                }
                newLine("}")
            }
            .newLine("}")

        Then
        writer.toString() shouldBe """class Example {
    get method() {
        println("Hello World")
    }
}"""
    }

    "chaining operations returns same writer instance" {
        Given
        val writer = CodeWriter()

        When
        val result = writer.write("test")
            .newLine(" line")
            .indent()
            .unindent()

        Then
        result shouldBe writer
    }

    "mixing write and newLine operations" {
        Given
        val writer = CodeWriter()

        When
        writer.write("start")
            .newLine(" line1")
            .write("inline")
            .newLine()
            .write("end")

        Then
        writer.toString() shouldBe "start${LINE_SEPARATOR} line1inline${LINE_SEPARATOR}end"
    }

    "if a multiline string is being written, don't remove the spacing in the string" {
        Given
        val writer = CodeWriter()
        val content = " = $TRIPLE_QUOTE\n                </ul>$TRIPLE_QUOTE"

        When
        writer.write(content)

        Then
        println(writer.toString())
        writer.toString() shouldBe content
    }
})
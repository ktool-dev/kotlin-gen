package dev.ktool.gen.types

import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class ComplexGenerationSpec : BddSpec({
    "file with class containing properties" {
        Given
        val file = KotlinFile("com.example.model") {
            +Class("User") {
                +Property("id", mutable = true, type = LongType)
                +Property("name", mutable = true, type = StringType)
                +Property("email", mutable = true, type = StringType)
            }
        }

        When
        val output = file.render()

        Then
        output shouldBe """
            package com.example.model

            class User {
                var id: Long
                var name: String
                var email: String
            }

        """.trimIndent()
    }

    "file with class containing properties and functions" {
        Given
        val file = KotlinFile("com.example.math") {
            +Class("Calculator") {
                +Property("result", mutable = true, type = IntType)

                +Function("add", returnType = IntType) {
                    +Parameter("a", IntType)
                    +Parameter("b", IntType)
                    body = ExpressionBody("a + b")
                }

                +Function("reset") {
                    body = FunctionBlock("result = 0")
                }
            }
        }

        When
        val output = file.render()

        Then
        output shouldBe """
            package com.example.math

            class Calculator {
                var result: Int

                fun add(a: Int, b: Int): Int = a + b

                fun reset() {
                    result = 0
                }
            }

        """.trimIndent()
    }

    "file with class containing properties, functions, and init block" {
        Given
        val file = KotlinFile("com.example.domain") {
            +Import("kotlin.String")

            +Class("Person") {
                +Property("firstName", type = StringType)
                +Property("lastName", type = StringType)
                +Property("fullName", mutable = true, type = StringType)

                +InitBlock("fullName = \"\$firstName \$lastName\"")

                +Function("greet", returnType = StringType) {
                    body = ExpressionBody("\"Hello, I'm \$fullName\"")
                }
            }
        }

        When
        val output = file.render()

        Then
        output shouldBe $$"""
            package com.example.domain

            import kotlin.String

            class Person {
                val firstName: String
                val lastName: String
                var fullName: String

                init {
                    fullName = "$firstName $lastName"
                }

                fun greet(): String = "Hello, I'm $fullName"
            }

        """.trimIndent()
    }

    "file with multiple classes and interfaces" {
        Given
        val file = KotlinFile("com.example.my") {
            +Import("com.example.model.User")
            +Import("kotlin.Long")

            +Interface("Repository") {
                +TypeParameter("T")

                +Function("save") {
                    +Parameter("entity", Type("T"))
                }

                +Function("findById", returnType = Type("T", nullable = true)) {
                    +Parameter("id", Type("Long"))
                }
            }

            +Class("UserRepository") {
                +SuperType("Repository") {
                    +TypeArgument("User")
                }

                +Function("save") {
                    +Modifier.Override
                    +Parameter("entity", Type("User"))
                    body = FunctionBlock("// Save user to database")
                }
            }
        }

        When
        val output = file.render()

        Then
        output shouldBe """
            package com.example.my

            import com.example.model.User
            import kotlin.Long

            interface Repository<T> {
                fun save(entity: T)

                fun findById(id: Long): T?
            }

            class UserRepository : Repository<User> {
                override fun save(entity: User) {
                    // Save user to database
                }
            }

        """.trimIndent()
    }

    "file with top-level properties and functions" {
        Given
        val file = KotlinFile("com.example.utils") {
            +Property("DEFAULT_TIMEOUT", type = Type("Long")) {
                +listOf(Modifier.Const)
                initializer = ExpressionBody("5000L")
            }

            +Function("formatMessage", returnType = StringType) {
                +Parameter("message", StringType)
                body = ExpressionBody($$"\"[APP] $message\"")
            }
        }

        When
        val output = file.render()

        Then
        output shouldBe $$"""
            package com.example.utils

            const val DEFAULT_TIMEOUT: Long = 5000L

            fun formatMessage(message: String): String = "[APP] $message"

        """.trimIndent()
    }

    "large file with many declarations" {
        Given
        val file = KotlinFile("com.example.ecommerce") {
            +Import("kotlin.collections.mutableListOf")
            +Import("kotlin.collections.List")
            +Import("kotlin.collections.MutableList")

            +Class("Product") {
                +Modifier.Data
                primaryConstructor = PrimaryConstructor {
                    +Property("id", type = StringType)
                    +Property("name", type = StringType)
                    +Property("price", type = DoubleType)
                }
            }

            +Interface("ProductService") {
                +Function("getAllProducts", returnType = Type("List", TypeArgument("Product")))

                +Function("getProductById", returnType = Type("Product?")) {
                    +Parameter("id", StringType)
                    body = FunctionBlock(
                        "val a = 10",
                        """
                            if(a != 10) {
                                a++
                            }
                        """.trimIndent(),
                        "return findProductById(id)"
                    )
                }
            }

            +Class("ProductServiceImpl") {
                +SuperType("ProductService")

                +Property("products", mutable = true, type = Type("MutableList", TypeArgument("Product")))

                +InitBlock("products = mutableListOf()")

                +Function("getAllProducts", returnType = Type("List", TypeArgument("Product"))) {
                    +listOf(Modifier.Override)
                    body = ExpressionBody("products")
                }
            }
        }

        When
        val output = file.render()

        Then
        output shouldBe """
            package com.example.ecommerce

            import kotlin.collections.List
            import kotlin.collections.MutableList
            import kotlin.collections.mutableListOf

            data class Product(val id: String, val name: String, val price: Double)

            interface ProductService {
                fun getAllProducts(): List<Product>

                fun getProductById(id: String): Product? {
                    val a = 10
                    if(a != 10) {
                        a++
                    }
                    return findProductById(id)
                }
            }

            class ProductServiceImpl : ProductService {
                var products: MutableList<Product>

                init {
                    products = mutableListOf()
                }

                override fun getAllProducts(): List<Product> = products
            }

        """.trimIndent()
    }
})

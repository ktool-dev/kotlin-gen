package dev.ktool.gen.types

import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class ComplexGenerationSpec : BddSpec({
    "file with class containing properties" {
        Given
        val file = KotlinFile("com.example.model") {
            clas("User") {
                property("id", type = Type("Long"))
                property("name", type = StringType)
                property("email", type = StringType)
            }
        }

        When
        val output = file.render()

        Then
        output shouldBe """
            package com.example.model

            class User {
                val id: Long
                val name: String
                val email: String
            }

        """.trimIndent()
    }

    "file with class containing properties and functions" {
        Given
        val file = KotlinFile("com.example.math") {
            clas("Calculator") {
                varProperty("result", type = IntType)

                function("add", returnType = IntType) {
                    param("a", IntType)
                    param("b", IntType)
                    body = ExpressionBody("a + b")
                }

                function("reset") {
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
            import("kotlin.String")

            clas("Person") {
                property("firstName", type = StringType)
                property("lastName", type = StringType)
                varProperty("fullName", type = StringType)

                init("fullName = \"\$firstName \$lastName\"")

                function("greet", returnType = StringType) {
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
            import("com.example.model.User")
            import("kotlin.Long")

            inter("Repository") {
                typeParam("T")

                function("save") {
                    param("entity", Type("T"))
                }

                function("findById", returnType = Type("T", nullable = true)) {
                    param("id", Type("Long"))
                }
            }

            clas("UserRepository") {
                superType("Repository") {
                    typeArg("User")
                }

                function("save") {
                    modifier(Modifier.Override)
                    param("entity", Type("User"))
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
            property("DEFAULT_TIMEOUT", type = Type("Long")) {
                modifier(Modifier.Const)
                initializer = ExpressionBody("5000L")
            }

            function("formatMessage", returnType = StringType) {
                param("message", StringType)
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
            import("kotlin.collections.mutableListOf")
            import("kotlin.collections.List")
            import("kotlin.collections.MutableList")

            clas("Product") {
                modifier(Modifier.Data)
                primaryConstructor = PrimaryConstructor {
                    valProperty("id", type = StringType)
                    valProperty("name", type = StringType)
                    valProperty("price", type = DoubleType)
                }
            }

            inter("ProductService") {
                function("getAllProducts", returnType = Type("List", Type("Product")))

                function("getProductById", returnType = Type("Product?")) {
                    param("id", StringType)
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

            clas("ProductServiceImpl") {
                superType("ProductService")

                varProperty("products", type = Type("MutableList", Type("Product")))

                init("products = mutableListOf()")

                function("getAllProducts", returnType = Type("List", Type("Product"))) {
                    modifier(Modifier.Override)
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

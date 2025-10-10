package dev.ktool.gen.types

import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class ComplexGenerationSpec : BddSpec({
    "file with class containing properties" {
        Given
        val file = KotlinFile("com.example.model") {
            addClass("User") {
                addValProperty("id", type = Type("Long"))
                addValProperty("name", type = StringType)
                addValProperty("email", type = StringType)
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
            addClass("Calculator") {
                addVarProperty("result", type = IntType)

                addFunction("add", returnType = IntType) {
                    addParameter("a", IntType)
                    addParameter("b", IntType)
                    body = ExpressionBody("a + b")
                }

                addFunction("reset") {
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
            addImport("kotlin.String")

            addClass("Person") {
                addValProperty("firstName", type = StringType)
                addValProperty("lastName", type = StringType)
                addVarProperty("fullName", type = StringType)

                addInit("fullName = \"\$firstName \$lastName\"")

                addFunction("greet", returnType = StringType) {
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
            addImport("com.example.model.User")
            addImport("kotlin.Long")

            addInterface("Repository") {
                addTypeParameter("T")

                addFunction("save") {
                    addParameter("entity", Type("T"))
                }

                addFunction("findById", returnType = Type("T", nullable = true)) {
                    addParameter("id", Type("Long"))
                }
            }

            addClass("UserRepository") {
                addSuperType("Repository") {
                    addTypeArgument("User")
                }

                addFunction("save") {
                    addModifiers(Modifier.Override)
                    addParameter("entity", Type("User"))
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
            addValProperty("DEFAULT_TIMEOUT", type = Type("Long")) {
                addModifiers(Modifier.Const)
                initializer = ExpressionBody("5000L")
            }

            addFunction("formatMessage", returnType = StringType) {
                addParameter("message", StringType)
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
            addImport("kotlin.collections.mutableListOf")
            addImport("kotlin.collections.List")
            addImport("kotlin.collections.MutableList")

            addClass("Product") {
                addModifiers(Modifier.Data)
                primaryConstructor = PrimaryConstructor {
                    addValProperty("id", type = StringType)
                    addValProperty("name", type = StringType)
                    addValProperty("price", type = DoubleType)
                }
            }

            addInterface("ProductService") {
                addFunction("getAllProducts", returnType = Type("List", Type("Product")))

                addFunction("getProductById", returnType = Type("Product?")) {
                    addParameter("id", StringType)
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

            addClass("ProductServiceImpl") {
                addSuperType("ProductService")

                addVarProperty("products", type = Type("MutableList", Type("Product")))

                addInit("products = mutableListOf()")

                addFunction("getAllProducts", returnType = Type("List", Type("Product"))) {
                    addModifiers(Modifier.Override)
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

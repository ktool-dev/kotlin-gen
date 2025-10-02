package dev.ktool.gen.declarations

import dev.ktool.gen.types.*
import dev.ktool.kotest.BddSpec
import io.kotest.matchers.shouldBe

class ComplexGenerationSpec : BddSpec({
    "file with class containing properties" {
        Given
        val cls = Class("User")
        cls.members.add(Property("id", type = Type("Long")))
        cls.members.add(Property("name", type = StringType))
        cls.members.add(Property("email", type = StringType))

        val file = KotlinFile(
            packageName = "com.example.model",
            topLevelDeclarations = listOf(cls)
        )

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
        val cls = Class("Calculator")
        cls.members.add(Property("result", type = IntType, mutability = PropertyMutability.VAR))
        cls.members.add(
            Function(
                name = "add",
                parameters = listOf(
                    Parameter("a", IntType),
                    Parameter("b", IntType)
                ),
                returnType = IntType,
                body = ExpressionBody("a + b")
            )
        )
        cls.members.add(
            Function(
                name = "reset",
                body = FunctionBlock(mutableListOf("result = 0"))
            )
        )

        val file = KotlinFile(
            packageName = "com.example.math",
            topLevelDeclarations = listOf(cls)
        )

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
        val cls = Class("Person")
        cls.members.add(Property("firstName", type = StringType))
        cls.members.add(Property("lastName", type = StringType))
        cls.members.add(Property("fullName", type = StringType, mutability = PropertyMutability.VAR))

        val initBlock = InitBlock()
        initBlock.statements.add("fullName = \"\$firstName \$lastName\"")
        cls.members.add(initBlock)

        cls.members.add(
            Function(
                name = "greet",
                returnType = StringType,
                body = ExpressionBody("\"Hello, I'm \$fullName\"")
            )
        )

        val file = KotlinFile(
            packageName = "com.example.domain",
            imports = listOf(Import("kotlin.String")),
            topLevelDeclarations = listOf(cls)
        )

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
        val repository = Interface("Repository")
        repository.typeParameters.add(TypeParameter("T"))
        repository.members.add(Function("save", parameters = listOf(Parameter("entity", Type("T")))))
        repository.members.add(
            Function(
                "findById",
                parameters = listOf(Parameter("id", Type("Long"))),
                returnType = Type("T", nullable = true)
            )
        )

        val userRepo = Class("UserRepository")
        userRepo.superTypes.add(Type("Repository", typeArguments = listOf(Type("User"))))
        userRepo.members.add(
            Function(
                name = "save",
                modifiers = listOf(Modifier.OVERRIDE),
                parameters = listOf(Parameter("entity", Type("User"))),
                body = FunctionBlock(mutableListOf("// Save user to database"))
            )
        )

        val file = KotlinFile(
            packageName = "com.example.my",
            imports = listOf(
                Import("com.example.model.User"),
                Import("kotlin.Long")
            ),
            topLevelDeclarations = listOf(repository, userRepo)
        )

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
        val constant = Property(
            name = "DEFAULT_TIMEOUT",
            type = Type("Long"),
            initializer = ExpressionBody("5000L"),
            modifiers = listOf(Modifier.CONST)
        )

        val helperFunc = Function(
            name = "formatMessage",
            parameters = listOf(Parameter("message", StringType)),
            returnType = StringType,
            body = ExpressionBody($$"\"[APP] $message\"")
        )

        val file = KotlinFile(
            packageName = "com.example.utils",
            topLevelDeclarations = listOf(constant, helperFunc)
        )

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
        val dataClass = Class("Product").apply {
            modifiers.add(Modifier.DATA)
            primaryConstructor = PrimaryConstructor(
                properties = listOf(
                    Property("id", type = StringType),
                    Property("name", type = StringType),
                    Property("price", type = DoubleType)
                ),
            )
        }

        val service = Interface("ProductService")
        service.members.add(
            Function(
                "getAllProducts",
                returnType = Type("List", typeArguments = listOf(Type("Product")))
            )
        )
        service.members.add(
            Function(
                "getProductById",
                parameters = listOf(Parameter("id", StringType)),
                returnType = Type("Product?"),
                body = FunctionBlock(
                    listOf(
                        "val a = 10",
                        """
                        if(a != 10) {
                            a++
                        }
                    """.trimIndent(),
                        "return findProductById(id)"
                    )
                )
            )
        )

        val impl = Class("ProductServiceImpl")
        impl.superTypes.add(Type("ProductService"))
        impl.members.add(
            Property(
                "products",
                type = Type("MutableList", typeArguments = listOf(Type("Product"))),
                mutability = PropertyMutability.VAR
            )
        )

        val initBlock = InitBlock()
        initBlock.statements.add("products = mutableListOf()")
        impl.members.add(initBlock)

        impl.members.add(
            Function(
                name = "getAllProducts",
                modifiers = listOf(Modifier.OVERRIDE),
                returnType = Type("List", typeArguments = listOf(Type("Product"))),
                body = ExpressionBody("products")
            )
        )

        val file = KotlinFile(
            packageName = "com.example.ecommerce",
            imports = listOf(
                Import("kotlin.collections.mutableListOf"),
                Import("kotlin.collections.List"),
                Import("kotlin.collections.MutableList"),
            ),
            topLevelDeclarations = listOf(dataClass, service, impl)
        )

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

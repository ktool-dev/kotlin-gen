# Kotlin Gen

A Kotlin Multiplatform code generation library for programmatically creating Kotlin source files.

## Overview

Kotlin Gen provides a type-safe API for generating Kotlin code. Instead of string concatenation or templates, you can
build Kotlin declarations using a fluent API that ensures proper formatting, indentation, and syntax.

## Features

- **Type-safe code generation**: Build Kotlin code using classes and builders
- **Multiplatform support**: Works on JVM, Android, iOS, Linux, Windows, and macOS
- **Comprehensive**: Supports classes, interfaces, functions, properties, and more
- **Automatic formatting**: Handles indentation, line breaks, and keyword escaping
- **Import management**: Automatically organize and sort imports

## Installation

```kotlin
dependencies {
    implementation("dev.ktool:kotlin-gen:0.0.0")
}
```

## Quick Start

### Creating a Simple Class

```kotlin
val file = KotlinFile("com.example") {
    clas("User") {
        property("id", type = LongType)
        property("name", type = StringType)
    }
}

println(file.render())
```

Output:

```kotlin
package com.example

class User {
    val id: Long
    val name: String
}
```

### Creating Functions

```kotlin
val file = KotlinFile("com.example") {
    function("add", returnType = IntType) {
        param("a", IntType)
        param("b", IntType)
        body = ExpressionBody("a + b")
    }
}

println(file.render())
// Output: fun add(a: Int, b: Int): Int = a + b
```

### Extension Functions

```kotlin
val file = KotlinFile("com.example") {
    function("isEven", returnType = BooleanType) {
        receiver(IntType)
        body = ExpressionBody("this % 2 == 0")
    }
}

println(file.render())
// Output: fun Int.isEven(): Boolean = this % 2 == 0
```

### Creating Interfaces

```kotlin
val file = KotlinFile("com.example") {
    interfac("Repository") {
        typeParam("T")

        function("save") {
            param("entity", Type("T"))
        }
    }
}

println(file.render())
```

Output:

```kotlin
interface Repository<T> {
    fun save(entity: T)
}
```

### Complete File with Imports

```kotlin
val file = KotlinFile("com.example.domain") {
    import("kotlin.collections.List")
    import("kotlin.collections.Map")

    clas("Product") {
        modifier(Modifier.Data)
        primaryConstructor {
            property("id", type = StringType)
            property("name", type = StringType)
            property("price", type = DoubleType)
        }
    }
}

println(file.render())
```

Output:

```kotlin
package com.example.domain

import kotlin.collections.List
import kotlin.collections.Map

data class Product(val id: String, val name: String, val price: Double)
```

## Core Components

### KotlinFile

Represents a complete Kotlin source file with package declaration, imports, and top-level declarations.

### Declarations

- **Class**: Standard and data classes with properties, functions, and init blocks
- **Interface**: Interfaces with abstract functions and properties
- **Object**: Singleton objects
- **Function**: Top-level and member functions with various modifiers
- **Property**: Top-level and member properties with getters/setters
- **TypeAlias**: Type aliases

### Type System

- **Type**: Represents Kotlin types including generics
- **TypeParameter**: Generic type parameters with variance
- **Parameter**: Function parameters with default values
- **Modifier**: Access modifiers (public, private, etc.) and other modifiers (override, suspend, etc.)

### Function Bodies

- **ExpressionBody**: Single-expression function bodies (`= expression`)
- **FunctionBlock**: Multi-statement function bodies with braces

### Features

- **Automatic keyword escaping**: Names that conflict with Kotlin keywords are automatically wrapped in backticks
- **Import sorting**: Imports are automatically sorted alphabetically
- **Proper indentation**: All nested structures are properly indented
- **Init blocks**: Support for class initialization blocks
- **Primary constructors**: Constructor parameters with properties

## Supported Platforms

- JVM
- Android (API 21+)
- iOS (x64, ARM64, Simulator ARM64)
- Linux (x64)
- Windows (mingw x64)
- macOS (x64, ARM64)

## License

Apache License 2.0

## Author

Aaron Freeman (aaron@ktool.dev)

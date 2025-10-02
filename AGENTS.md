# AI Agents Documentation

This document provides guidance for AI agents working with the Kotlin Gen codebase.

## Project Structure

```
kotlin-gen/
├── libraries/
│   └── core/
│       └── src/
│           ├── commonMain/kotlin/dev/ktool/gen/
│           │   ├── CodeWriter.kt          # Core rendering engine
│           │   ├── declarations/          # Kotlin declaration types
│           │   └── types/                 # Type system components
│           └── commonTest/kotlin/dev/ktool/gen/
│               └── declarations/          # Tests using BddSpec
├── build.gradle.kts
└── settings.gradle.kts
```

## Architecture Overview

### Core Concepts

1. **Writable Interface**: All code generation elements implement `Writable` with:
   - `write(writer: CodeWriter)`: Writes the element to a CodeWriter
   - `render(): String`: Convenience method that returns the generated code as a string

2. **CodeWriter**: Central rendering engine that:
   - Manages indentation levels
   - Handles line breaks and formatting
   - Provides utility methods for writing code blocks

3. **Type System**: Hierarchical type system with:
   - `Type`: Represents Kotlin types including generics
   - `TypeParameter`: Generic type parameters with variance
   - `Parameter`: Function/constructor parameters
   - `Modifier`: Visibility and behavior modifiers
   - `PropertyMutability`: val/var distinction

4. **Declarations**: Code elements that can appear in files:
   - `TopLevelDeclaration`: Classes, interfaces, objects, functions, properties, type aliases
   - `ClassMember`: Properties, functions, init blocks, constructors

## Key Classes

### KotlinFile
The root element representing a complete `.kt` file.

**Properties:**
- `packageName: String?` - Package declaration (nullable)
- `imports: MutableList<Import>` - Import statements (auto-sorted)
- `topLevelDeclarations: MutableList<TopLevelDeclaration>` - Classes, functions, etc.

**Behavior:**
- Imports are sorted alphabetically by package path
- Declarations are separated by blank lines
- Output always ends with a line separator

### Class
Represents Kotlin classes (including data classes).

**Properties:**
- `name: String` - Class name
- `modifiers: MutableList<Modifier>` - Access/behavior modifiers
- `typeParameters: MutableList<TypeParameter>` - Generic type parameters
- `primaryConstructor: PrimaryConstructor?` - Primary constructor
- `superTypes: MutableList<Type>` - Parent classes/interfaces
- `members: MutableList<ClassMember>` - Properties, functions, init blocks

### Function
Represents functions (top-level or member functions).

**Properties:**
- `name: String` - Function name
- `receiver: Type?` - Receiver type for extension functions
- `modifiers: MutableList<Modifier>` - Modifiers (inline, suspend, etc.)
- `typeParameters: MutableList<TypeParameter>` - Generic parameters
- `parameters: MutableList<Parameter>` - Function parameters
- `returnType: Type?` - Return type (nullable for Unit)
- `body: FunctionBody?` - Function body (ExpressionBody or FunctionBlock)

### Property
Represents properties (top-level or member properties).

**Properties:**
- `name: String` - Property name
- `type: Type?` - Property type
- `mutability: PropertyMutability` - val or var
- `initializer: ExpressionBody?` - Initialization expression
- `getter: PropertyGetter?` - Custom getter
- `setter: PropertySetter?` - Custom setter

**Requirements:**
- Must have either `type` or `initializer` (or both)

## Code Generation Patterns

### Pattern 1: Building Declarations Programmatically

```kotlin
val cls = Class("User")
cls.members.add(Property("id", type = Type("Long")))
cls.members.add(Property("name", type = Type("String")))
```

### Pattern 2: Using Constructor Parameters

```kotlin
val cls = Class(
    name = "User",
    members = listOf(
        Property("id", type = Type("Long")),
        Property("name", type = Type("String"))
    )
)
```

### Pattern 3: Rendering Individual Elements

```kotlin
val function = Function("test")
val code = function.render() // Returns: "fun test()"
```

### Pattern 4: Complete File Generation

```kotlin
val file = KotlinFile(
    packageName = "com.example",
    imports = listOf(Import("kotlin.String")),
    topLevelDeclarations = listOf(cls)
)
val sourceCode = file.render()
```

## Testing Guidelines

### Test Framework
Tests use **Kotest BddSpec** with Given/When/Then structure:

```kotlin
class FunctionSpec : BddSpec({
    "simple function with no parameters" {
        Given
        val func = Function("doSomething")

        When
        val output = func.render()

        Then
        output shouldBe "fun doSomething()"
    }
})
```

### Test Naming
- Test file names end with `Spec.kt` (e.g., `FunctionSpec.kt`)
- Test names are descriptive strings in lowercase with spaces
- Located in `src/commonTest/kotlin/dev/ktool/gen/declarations/`

### What to Test
1. **Basic functionality**: Simple cases with minimal configuration
2. **Combinations**: Multiple modifiers, parameters, type parameters
3. **Edge cases**: Keywords requiring backticks, empty lists
4. **Mutability**: Testing that properties can be modified after construction
5. **Complex scenarios**: Real-world examples with multiple features combined

## Important Implementation Details

### Keyword Escaping
The `safe` extension property automatically wraps Kotlin keywords in backticks:
```kotlin
val name = "class"
name.safe // Returns: "`class`"
```

This is applied to:
- Identifiers (class names, function names, property names)
- Package path segments (via `safePackage`)

### Indentation Management
CodeWriter uses `withIndent {}` for nested blocks:
```kotlin
writer.write("class Foo {")
writer.withIndent {
    writer.newLine()
    writer.write("val x: Int")
}
writer.newLine()
writer.write("}")
```

### Line Separator
Use `LINE_SEPARATOR` constant (defined as `"\n"`) for all line breaks to ensure consistency across platforms.

## Common Tasks for Agents

### Adding a New Declaration Type
1. Create class implementing `TopLevelDeclaration` or `ClassMember`
2. Implement `write(writer: CodeWriter)` method
3. Add test file in `commonTest/` following existing patterns
4. Update this documentation

### Adding a New Modifier
1. Add enum value to `Modifier` enum in `types/Modifier.kt`
2. Ensure `keyword` property is set correctly
3. Add test cases to relevant declaration tests

### Fixing a Rendering Issue
1. Locate the `write()` method in the relevant class
2. Update the CodeWriter calls
3. Add/update tests to cover the case
4. Verify existing tests still pass

### Understanding Existing Code
1. Start with `KotlinFile` and `CodeWriter` to understand the core
2. Look at test files (`*Spec.kt`) for usage examples
3. Check `render()` calls in tests to see expected output
4. Trace through `write()` methods to understand rendering logic

## Development Workflow

1. **Read existing tests** to understand current behavior
2. **Write tests first** for new features or bug fixes
3. **Implement minimal changes** to make tests pass
4. **Verify all tests pass** (use `./gradlew test`)
5. **Update documentation** if adding new features

## Build Commands

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests "FunctionSpec"

# Build the project
./gradlew build

# Clean build
./gradlew clean build
```

## Dependencies

- **Kotlin Multiplatform**: Core framework
- **Kotest**: Testing framework with BddSpec
- **kotlin-logging**: Logging (if needed)
- **okio**: File I/O for native platforms

## Notes for AI Agents

1. **Prefer reading test files** to understand how classes are used
2. **Follow existing patterns** for consistency
3. **Test thoroughly** - add tests for edge cases and combinations
4. **Keep it simple** - avoid over-engineering
5. **Document new features** in README.md and this file
6. **Use `render()` in tests** for readable assertions
7. **Check for keyword conflicts** when dealing with names
8. **Remember mutable lists** - properties like `members`, `modifiers` are mutable

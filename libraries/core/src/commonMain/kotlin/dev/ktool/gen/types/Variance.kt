package dev.ktool.gen.types

enum class Variance {
    IN, OUT;

    override fun toString(): String = name.lowercase()
}
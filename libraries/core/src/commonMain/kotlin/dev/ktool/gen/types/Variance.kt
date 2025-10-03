package dev.ktool.gen.types

enum class Variance {
    In, Out;

    override fun toString(): String = name.lowercase()
}
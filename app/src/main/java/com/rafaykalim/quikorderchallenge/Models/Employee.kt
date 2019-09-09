package com.rafaykalim.quikorderchallenge.Models

data class Employee(var name: String, var title: String, var locations: Array<String>) {

    // Boilerplate due to array
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Employee

        if (name != other.name) return false
        if (!locations.contentEquals(other.locations)) return false
        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + locations.contentHashCode()
        result = 31 * result + title.hashCode()
        return result
    }
}
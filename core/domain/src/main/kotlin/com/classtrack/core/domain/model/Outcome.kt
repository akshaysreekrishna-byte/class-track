package com.classtrack.core.domain.model

/**
 * A typed result carrier for operations that may fail across layer boundaries.
 * Replaces bare exceptions and aligns with AGENTS.md §2.4.
 */
sealed class Outcome<out T> {
    data class Success<T>(val data: T) : Outcome<T>()
    data class Error(val message: String, val cause: Throwable? = null) : Outcome<Nothing>()
}

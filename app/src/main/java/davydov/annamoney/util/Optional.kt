package davydov.annamoney.util

data class Optional<out T>(val value: T?) {
    companion object {
        fun <T> empty() = Optional<T>(null)
    }
}

fun <T> T?.optional() = Optional(this)
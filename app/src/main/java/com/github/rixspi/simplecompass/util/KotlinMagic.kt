package com.github.rixspi.simplecompass.util


fun <T : Any> arrayOfNotNullOrNull(vararg elements: T?): Array<T>? {
    return if (elements.contains(null)) null else elements as Array<T>
}
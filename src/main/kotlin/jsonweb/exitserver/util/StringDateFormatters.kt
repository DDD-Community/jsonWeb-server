package jsonweb.exitserver.util

fun String.toDotFormat() = this
    .split(" ")[0]
    .replace("-", ".")


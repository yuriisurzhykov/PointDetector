package com.yuriisurzhykov.foodbanks.core.data

/**
 * Typed access to a value read from the cloud database. Firebase returns objects as maps
 * and arrays as lists, so the raw value is wrapped instead of being mapped to beans, which
 * don't know anything about the serial names of the cloud models.
 */
interface SnapshotValue {

    @kotlin.jvm.Throws(MappingException::class)
    fun string(key: String): String

    fun stringOrEmpty(key: String): String

    @kotlin.jvm.Throws(MappingException::class)
    fun double(key: String): Double

    @kotlin.jvm.Throws(MappingException::class)
    fun int(key: String): Int

    @kotlin.jvm.Throws(MappingException::class)
    fun value(key: String): SnapshotValue

    fun values(key: String): List<SnapshotValue>

    class Base(private val raw: Map<*, *>) : SnapshotValue {

        override fun string(key: String): String =
            raw[key] as? String ?: throw MappingException(raw, String::class)

        override fun stringOrEmpty(key: String): String = raw[key] as? String ?: ""

        override fun double(key: String): Double =
            (raw[key] as? Number)?.toDouble() ?: throw MappingException(raw, Double::class)

        override fun int(key: String): Int =
            (raw[key] as? Number)?.toInt() ?: throw MappingException(raw, Int::class)

        override fun value(key: String): SnapshotValue =
            Base(raw[key] as? Map<*, *> ?: throw MappingException(raw, SnapshotValue::class))

        override fun values(key: String): List<SnapshotValue> {
            val children = when (val value = raw[key]) {
                is List<*> -> value
                is Map<*, *> -> value.values
                else -> emptyList<Any>()
            }
            return children.filterIsInstance<Map<*, *>>().map { Base(it) }
        }
    }
}

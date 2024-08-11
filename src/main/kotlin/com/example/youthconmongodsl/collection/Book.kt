package com.example.youthconmongodsl.collection

data class Book(
    var title: String,
    var price: Long,
    var isbn: String,
    var description: String? = null,
) : MutableMap<String, Any?> {
    private val map: MutableMap<String, Any?> = mutableMapOf(
        "title" to title,
        "price" to price,
        "isbn" to isbn,
        "description" to description
    )

    constructor() : this(
        title = "",
        price = 0,
        isbn = "",
        description = null,
    )

    override val entries: MutableSet<MutableMap.MutableEntry<String, Any?>>
        get() = map.entries

    override val keys: MutableSet<String>
        get() = map.keys

    override val size: Int
        get() = map.size

    override val values: MutableCollection<Any?>
        get() = map.values

    override fun clear() {
        map.clear()
    }

    override fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    override fun remove(key: String): Any? {
        return map.remove(key)
    }

    override fun putAll(from: Map<out String, Any?>) {
        map.putAll(from)
    }

    override fun put(key: String, value: Any?): Any? {
        return when (key) {
            "title" -> {
                title = value as String
                map.put(key, value)
            }

            "price" -> {
                price = value as Long
                map.put(key, value)
            }

            "isbn" -> {
                isbn = value as String
                map.put(key, value)
            }

            "description" -> {
                description = value as String?
                map.put(key, value)
            }

            else -> map.put(key, value)
        }
    }

    override fun get(key: String): Any? {
        return map[key]
    }

    override fun containsValue(value: Any?): Boolean {
        return map.containsValue(value)
    }

    override fun containsKey(key: String): Boolean {
        return map.containsKey(key)
    }

    companion object {
        fun of(
            title: String,
            price: Long,
            isbn: String,
            description: String? = null,
        ) = Book(
            title = title,
            price = price,
            isbn = isbn,
            description = description,
        )
    }
}
package nz.co.trademe.techtest.utils


interface AdapterObserver<in T> {
    infix fun clearAndAddAll(collection: Collection<T>)

    infix fun safeClearAndAddAll(collection: Collection<T>?)

    fun add(item: T)

    fun add(item: T, index: Int)

    fun addAll(collection: Collection<T>, index: Int)

    fun addAll(collection: Collection<T>)

    fun addAll(list: List<T>)

    fun safeAddAll(collection: Collection<T>?)

    fun remove(index: Int)

    fun remove(item: T)

    fun removeRange(vararg item: T)

    fun removeRange(collection: Collection<T>)

    fun update(index: Int, item: T)

    fun updateAll(collection: Collection<T>)

    fun safeUpdateAll(collection: Collection<T>?)

    fun updateRange(vararg item: T)

    fun updateRange(collection: List<T>)

    fun clear()

    fun clearAndAddAll(collection: List<T>)
}

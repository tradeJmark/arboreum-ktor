internal fun <T> List<T>.headPartition(): Pair<T, List<T>> {
    return get(0) to drop(1)
}
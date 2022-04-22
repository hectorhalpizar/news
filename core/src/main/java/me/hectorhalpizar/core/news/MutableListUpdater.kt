package me.hectorhalpizar.core.news

/**
 * Updates current [MutableList] with new inputs sequentially
 *
 * @param newInputList: List with new date to update
 */
fun <E> MutableList<E>.update(newInputList: List<E>) {
    newInputList.forEach {
        val index = this.lastIndexOf(it)
        if (index == -1) {
            this.add(it)
        } else {
            this[index] = it
        }
    }
}
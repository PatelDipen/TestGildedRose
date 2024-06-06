package com.gildedrose

class GildedRose(private var items: List<Item>) {

    fun updateQuality() {
        items.map {
            val change = it.updatedValue()
            it.apply {
                sellIn = change.first
                quality = change.second
            }
        }
    }
}


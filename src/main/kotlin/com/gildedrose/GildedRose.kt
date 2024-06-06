package com.gildedrose

class GildedRose(private var items: List<Item>) {

    fun updateQuality() {
        items.map { item ->
            val change = item.updatedValue()
            item.apply {
                // TODO .SELL_IN & .QUALITY should work here instead of .first and .second
                //  but it is dependent on kotlin version so keeping it simple
                sellIn = change.first
                quality = change.second
            }
        }
    }
}


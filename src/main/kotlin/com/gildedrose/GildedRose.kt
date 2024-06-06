package com.gildedrose

class GildedRose(private var items: List<Item>) {

    fun updateQuality() {
        items.forEach { item ->
            item.updatedValue().also { (sellIn, quality) ->
                item.sellIn = sellIn
                item.quality = quality
            }
        }
    }
}


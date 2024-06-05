package com.gildedrose

const val DEXTERITY_VEST = "+5 Dexterity Vest"
const val AGED_BRIE = "Aged Brie"
const val ELIXIR_OF_THE_MONGOOSE = "Elixir of the Mongoose"
const val SULFURAS_HAND_OF_RAGNAROS = "Sulfuras, Hand of Ragnaros"
const val BACKSTAGE_PASSES_TO_A_TAFKAL80ETC_CONCERT = "Backstage passes to a TAFKAL80ETC concert"
const val CONJURED_MANA_CAKE = "Conjured Mana Cake"

sealed class Operation {
    data class Increase(val count: Int) : Operation()
    data class Decrease(val count: Int) : Operation()
    data object None : Operation()
}

private fun Item.isIncreasedQualityValid(incrementValue: Int): Boolean {
    return (quality + incrementValue) <= 50
}

private fun Item.isDecreasedQualityValid(decrementValue: Int): Boolean {
    return (quality - decrementValue) >= 0
}

/*
* Return the value that needs to be increase or decrease based on item type
* */
private fun Item.getQualityOperation(currentItemSellIn: Int): Operation {
    return when (name) {
        SULFURAS_HAND_OF_RAGNAROS -> {
            Operation.None
        }

        AGED_BRIE -> {
            val incrementValue = if (currentItemSellIn < 0) 2 else 1
            return Operation.Increase(incrementValue)
        }

        BACKSTAGE_PASSES_TO_A_TAFKAL80ETC_CONCERT -> {
            if (currentItemSellIn < 0) {
                Operation.Decrease(quality)
            } else if (currentItemSellIn < 5 && isIncreasedQualityValid(3)) { // TODO need to clarify if <= is required here
                Operation.Increase(3)
            } else if (currentItemSellIn < 10 && isIncreasedQualityValid(2)) { // TODO need to clarify if <= is required here
                Operation.Increase(2)
            } else if (isIncreasedQualityValid(1)) {
                Operation.Increase(1)
            } else {
                Operation.Increase(0)
            }
        }

        DEXTERITY_VEST, ELIXIR_OF_THE_MONGOOSE -> {
            val decrementValue = if (currentItemSellIn < 0) 2 else 1
            Operation.Decrease(decrementValue)
        }

        CONJURED_MANA_CAKE -> {
            var decrementValue = if (currentItemSellIn < 0) 2 else 1
            decrementValue *= 2                                                                 // TODO need to clarify if *2 is required here
            Operation.Decrease(decrementValue)
        }

        else -> {
            Operation.None
        }
    }
}

typealias SELL_IN = Int
typealias QUALITY = Int

/*
* Return the updated value of SellIn & Quality
* */
fun Item.updatedValue(): Pair<SELL_IN, QUALITY> {
    with(this) {
        // Reducing SellIn day for all items other than SULFURAS_HAND_OF_RAGNAROS
        val sell = if (name == SULFURAS_HAND_OF_RAGNAROS) {
            0
        } else {
            -1
        }
        val currentItemSellIn = sellIn + sell

        val operation = getQualityOperation(currentItemSellIn)

        // Validating if after incrementing or decrementing quality value,
        // quality will match the given criteria else returning old quality
        val currentItemQuality = when (operation) {
            is Operation.Increase -> {
                if (isIncreasedQualityValid(operation.count)) {
                    quality + operation.count
                } else {
                    quality
                }
            }

            is Operation.Decrease -> {
                if (isDecreasedQualityValid(operation.count)) {
                    quality - operation.count
                } else {
                    quality
                }
            }

            is Operation.None -> {
                quality
            }
        }
        return Pair(currentItemSellIn, currentItemQuality)
    }
}
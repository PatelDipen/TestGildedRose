package com.gildedrose

const val DEXTERITY_VEST = "+5 Dexterity Vest"
const val AGED_BRIE = "Aged Brie"
const val ELIXIR_OF_THE_MONGOOSE = "Elixir of the Mongoose"
const val SULFURAS_HAND_OF_RAGNAROS = "Sulfuras, Hand of Ragnaros"
const val BACKSTAGE_PASSES_TO_A_TAFKAL80ETC_CONCERT = "Backstage passes to a TAFKAL80ETC concert"
const val CONJURED_MANA_CAKE = "Conjured Mana Cake"

sealed class Operation {
    data class Increase(val number: Int) : Operation()
    data class Decrease(val number: Int) : Operation()
    data object None : Operation()
}

private fun Item.isIncreasedQuantityValid(incrementValue: Int): Boolean {
    return (quality + incrementValue) <= 50
}

private fun Item.isDecreasedQuantityValid(decrementValue: Int): Boolean {
    return (quality - decrementValue) >= 0
}

private fun Item.getQuantityOperation(currentItemSellIn: Int): Operation {
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
            } else if (currentItemSellIn < 5 && isIncreasedQuantityValid(3)) { // TODO need to clarify if <= is required here
                Operation.Increase(3)
            } else if (currentItemSellIn < 10 && isIncreasedQuantityValid(2)) { // TODO need to clarify if <= is required here
                Operation.Increase(2)
            } else if (isIncreasedQuantityValid(1)) {
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

typealias SELLIN = Int
typealias QUALITY = Int

fun Item.updatedValue(): Pair<SELLIN, QUALITY> {
    with(this) {
        val sell = if (name == SULFURAS_HAND_OF_RAGNAROS) {
            0
        } else {
            -1
        }
        val currentItemSellIn = sellIn + sell

        val operation = getQuantityOperation(currentItemSellIn)

        val currentItemQuality = when (operation) {
            is Operation.Increase -> {
                if (isIncreasedQuantityValid(operation.number)) {
                    quality + operation.number
                } else {
                    quality
                }
            }

            is Operation.Decrease -> {
                if (isDecreasedQuantityValid(operation.number)) {
                    quality - operation.number
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
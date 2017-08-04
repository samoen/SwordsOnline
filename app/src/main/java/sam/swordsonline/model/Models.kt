package sam.swordsonline

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

data class Player(var name: String = "DefaultPlayer", var strength: Int = 5, var dexterity: Int = 5, var intelligence: Int = 5, var items: MutableList<Item> = mutableListOf<Item>(), var location: Pair<Int, Int> = Pair(11, 11), var gold: Int = 0, var equipped: MutableMap<Int, Item?> = mutableMapOf<Int, Item?>(), var current_speed: Int = 0, var equippedKeys: MutableMap<Int, Int> = mutableMapOf<Int, Int>(0 to 0, 1 to 1, 2 to 2, 3 to 3, 4 to 4)) {

    fun StatsMet(item: Item): Boolean = (this.strength in item.stat_requirement.strength_range && this.dexterity in item.stat_requirement.dexterity_range && this.intelligence in item.stat_requirement.intelligence_range)
    fun CanAfford(item: Item): Boolean = this.gold >= item.price
    fun CanChangeStat(direction: String, stat: String): Boolean {
        var canchange = true
        when (stat) {
            "strength" -> {
                for (v in this.equipped) {
                    if (direction == "increase") {
                        if (!((v.value?.stat_requirement?.strength_range)?.contains(this.strength + 1) ?: false)) {
                            canchange = false
                        }
                    } else if (direction == "decrease") {
                        if (!((v.value?.stat_requirement?.strength_range)?.contains(this.strength - 1) ?: false)) {
                            canchange = false
                        }
                    }
                }
            }
            "dexterity" -> {
                for (v in this.equipped) {
                    if (direction == "increase") {
                        if (!((v.value?.stat_requirement?.dexterity_range)?.contains(this.dexterity + 1) ?: false)) {
                            canchange = false
                        }
                    } else if (direction == "decrease") {
                        if (!((v.value?.stat_requirement?.dexterity_range)?.contains(this.dexterity - 1) ?: false)) {
                            canchange = false
                        }
                    }
                }
            }
            "intelligence" -> {
                for (v in this.equipped) {
                    if (direction == "increase") {
                        if (!((v.value?.stat_requirement?.intelligence_range)?.contains(this.intelligence + 1) ?: false)) {
                            canchange = false
                        }
                    } else if (direction == "decrease") {
                        if (!((v.value?.stat_requirement?.intelligence_range)?.contains(this.intelligence - 1) ?: false)) {
                            canchange = false
                        }
                    }
                }
            }
        }
        return canchange
    }
}

data class Item(var name: String = "default item", var stat_requirement: StatRequirement = StatRequirement(), var equipment_slot: Int = 0, var ability: Ability = Ability(), var price: Int = 0, var cooldown: Int = 1, var image_resource: Int = R.drawable.item_image_sword)
data class StatRequirement(var strength_range: IntRange = 0..11, var dexterity_range: IntRange = 0..11, var intelligence_range: IntRange = 0..11)
data class Ability(var type: String = "default ability", var relative_pairs: List<Pair<Int, Int>> = listOf(), var speed: Int = 0)

fun CalculatePairFromPosition(pos: Int): Pair<Int, Int> {
    val column = Math.ceil((pos.toDouble() + 1) / 10).toInt()
    val row = column * 10 - pos
    val mycol = Math.abs(column - 11)
    val myrow = Math.abs(row - 11)
    return Pair(myrow, mycol)
}

fun CalculatePositionFromPair(pair: Pair<Int, Int>): Int {
    when (pair.first) {
        1 -> return 100 - (pair.second * 10)
        2 -> return 101 - (pair.second * 10)
        3 -> return 102 - (pair.second * 10)
        4 -> return 103 - (pair.second * 10)
        5 -> return 104 - (pair.second * 10)
        6 -> return 105 - (pair.second * 10)
        7 -> return 106 - (pair.second * 10)
        8 -> return 107 - (pair.second * 10)
        9 -> return 108 - (pair.second * 10)
        10 -> return 109 - (pair.second * 10)
    }
    return 0
}

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken(), 0);
}
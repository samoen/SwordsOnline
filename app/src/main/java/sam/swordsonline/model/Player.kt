package sam.swordsonline.model

data class Player(var name: String = "DefaultPlayer", var strength: Int = 5, var dexterity: Int = 5, var intelligence: Int = 5, var items: MutableList<Item> = mutableListOf<Item>(), var location: Pair<Int, Int> = Pair(11, 11), var gold: Int = 0, var equipped: MutableMap<Int, Item?> = mutableMapOf<Int, Item?>(), var equippedKeys: MutableMap<Int, Int> = mutableMapOf<Int, Int>(0 to 0, 1 to 1, 2 to 2, 3 to 3, 4 to 4)) {

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
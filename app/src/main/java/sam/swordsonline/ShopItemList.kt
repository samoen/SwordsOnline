package sam.swordsonline
object ShopItemList {

    val allItems = mapOf<Int,Item>(
            0 to  Item(
                    name = "rusty helmet",
                    stat_requirement = StatRequirement(strength_range = 3..7,dexterity_range = 2..6,intelligence_range = 2..6),
                    equipment_slot = 0,
                    ability = Ability(type = "move", relative_pairs = listOf(Pair(0,1),Pair(1,0), Pair(-1,0),Pair(1,-1),Pair(-1,-1)),speed = 1),
                    price = 0,
                    cooldown = 3
            ),
            1 to Item(
                    name = "tattered clothes",
                    stat_requirement = StatRequirement(3..8,2..6,2..6),
                    equipment_slot = 1,
                    ability = Ability(type = "move", relative_pairs = listOf(Pair(2,0), Pair(-2,0)),speed = 2),
                    cooldown = 2,
                    price = 0
            ),
            2 to Item(
                    name = "old shoes",
                    stat_requirement = StatRequirement(3..9,2..6,2..6),
                    equipment_slot = 2,
                    ability = Ability("move", listOf(Pair(0,2),Pair(0,-1)),3),
                    price = 0,
                    cooldown = 2
            ),
            3 to Item(
                    name = "beaten shield",
                    stat_requirement = StatRequirement(3..8,2..6,2..6),
                    equipment_slot = 3,
                    ability = Ability("attack", listOf(Pair(0,1),Pair(1,0),Pair(-1,0)),3),
                    cooldown = 3,
                    price = 0
            ),
            4 to Item(
                    name = "dusty knife",
                    stat_requirement = StatRequirement(3..10,2..6,2..6),
                    equipment_slot = 4,
                    ability = Ability(type = "attack", relative_pairs = listOf(Pair(1,1),Pair(-1,1),Pair(0,-1),Pair(0,1)),speed = 4),
                    cooldown = 3,
                    price = 0
            ),
            5 to Item(
                    name = "sword of deadliness",
                    stat_requirement = StatRequirement(5..10,2..6,2..6),
                    equipment_slot = 4,
                    ability = Ability("attack", listOf(Pair(0,1),Pair(0,3),Pair(1,2),Pair(-1,2)),8),
                    cooldown = 1,
                    price = 6
            ),
            6 to Item(
                    name = "mage stick",
                    stat_requirement = StatRequirement(5..8,2..6,2..6),
                    equipment_slot = 4,
                    ability = Ability("attack", listOf(Pair(0,3),Pair(1,2),Pair(-1,2),Pair(-1,3),Pair(1,3)),3),
                    cooldown = 3,
                    price = 5
            ),
            7 to Item(
                    name = "cloth boots",
                    stat_requirement = StatRequirement(5..10,2..6,2..6),
                    equipment_slot = 2,
                    ability = Ability("move", listOf(Pair(0,1),Pair(0,3),Pair(1,2),Pair(-1,2)),3),
                    cooldown = 2,
                    price = 9
            ),
            8 to Item(
                    name = "plate legs",
                    stat_requirement = StatRequirement(5..8,2..6,2..6),
                    equipment_slot = 2,
                    ability = Ability("move", listOf(Pair(0,2),Pair(-1,3),Pair(1,2),Pair(-1,-2)),8),
                    cooldown = 2,
                    price = 3
            ),
            9 to Item(
                    name = "knight shield",
                    stat_requirement = StatRequirement(5..10,2..6,2..6),
                    equipment_slot = 3,
                    ability = Ability("attack", listOf(Pair(0,1),Pair(1,1),Pair(-1,0),Pair(1,0),Pair(-1,1)),5),
                    cooldown = 2,
                    price = 2
            ),
            10 to Item(
                    name = "great helm",
                    stat_requirement = StatRequirement(5..8,2..6,2..6),
                    equipment_slot = 0,
                    ability = Ability("move", listOf(Pair(-1,-3),Pair(0,-3),Pair(1,2),Pair(-1,2)),3),
                    cooldown = 1,
                    price = 7
            ),
            11 to Item(
                    name = "duelist buckler",
                    stat_requirement = StatRequirement(5..10,2..6,2..6),
                    equipment_slot = 3,
                    ability = Ability("attack", listOf(Pair(0,1),Pair(1,0),Pair(1,2),Pair(-1,2)),8),
                    cooldown = 1,
                    price = 2
            ),
            12 to Item(
                    name = "chainmail",
                    stat_requirement = StatRequirement(5..8,2..6,2..6),
                    equipment_slot = 1,
                    ability = Ability("move", listOf(Pair(-1,-1),Pair(-1,-2),Pair(1,2),Pair(-3,1),Pair(1,1),Pair(3,1)),2),
                    cooldown = 3,
                    price = 7
            )
    )
}
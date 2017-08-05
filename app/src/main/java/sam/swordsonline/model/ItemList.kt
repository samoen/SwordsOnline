package sam.swordsonline.model

import sam.swordsonline.R

class ItemList {

        val allItems = mapOf<Int,Item>(
                0 to  Item(
                        name = "Rusty Helmet",
                        stat_requirement = StatRequirement(strength_range = 3..7,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 0,
                        ability = Ability(type = "move", relative_pairs = listOf(Pair(0,1),Pair(1,0), Pair(-1,0),Pair(1,-1),Pair(-1,-1)),speed = 1),
                        price = 0,
                        cooldown = 3,
                        id = 0,
                        image_resource = R.drawable.item_image_helmet
                ),
                1 to Item(
                        name = "Tattered Clothes",
                        stat_requirement = StatRequirement(strength_range = 3..8,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 1,
                        ability = Ability(type = "move", relative_pairs = listOf(Pair(2,0), Pair(-2,0)),speed = 2),
                        cooldown = 2,
                        price = 0,
                        id = 1,
                        image_resource = R.drawable.item_image_shoulders
                ),
                2 to Item(
                        name = "Old Shoes",
                        stat_requirement = StatRequirement(strength_range = 3..9,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 2,
                        ability = Ability("move", listOf(Pair(0,2),Pair(0,-1)),3),
                        price = 0,
                        cooldown = 2,
                        id = 2,
                        image_resource = R.drawable.item_image_legs
                ),
                4 to Item(
                        name = "Dusty Knife",
                        stat_requirement = StatRequirement(strength_range = 3..10,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 4,
                        ability = Ability(type = "attack", relative_pairs = listOf(Pair(1,1),Pair(-1,1),Pair(0,-1),Pair(0,1)),speed = 4),
                        cooldown = 3,
                        price = 0,
                        id = 4,
                        image_resource = R.drawable.item_image_sword
                ),
                3 to Item(
                        name = "Beaten Shield",
                        stat_requirement = StatRequirement(strength_range = 3..8,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 3,
                        ability = Ability("attack", listOf(Pair(0,1),Pair(1,0),Pair(-1,0)),3),
                        cooldown = 3,
                        price = 0,
                        id = 3,
                        image_resource = R.drawable.item_image_shield

                ),

                5 to Item(
                        name = "Deadly Sword",
                        stat_requirement = StatRequirement(strength_range = 5..10,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 4,
                        ability = Ability("attack", listOf(Pair(0,1),Pair(0,3),Pair(1,2),Pair(-1,2)),8),
                        cooldown = 1,
                        price = 6,
                        id = 5,
                        image_resource = R.drawable.item_image_sword
                ),
                6 to Item(
                        name = "Mage Stick",
                        stat_requirement = StatRequirement(strength_range = 5..8,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 4,
                        ability = Ability("attack", listOf(Pair(0,3),Pair(1,2),Pair(-1,2),Pair(-1,3),Pair(1,3)),3),
                        cooldown = 3,
                        price = 5,
                        id = 6,
                        image_resource = R.drawable.item_image_sword
                ),
                7 to Item(
                        name = "Cloth Boots",
                        stat_requirement = StatRequirement(strength_range = 5..10,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 2,
                        ability = Ability("move", listOf(Pair(0,1),Pair(0,3),Pair(1,2),Pair(-1,2)),3),
                        cooldown = 2,
                        price = 9,
                        id = 7,
                        image_resource = R.drawable.item_image_legs
                ),
                8 to Item(
                        name = "Plate Legs",
                        stat_requirement = StatRequirement(strength_range = 5..8,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 2,
                        ability = Ability("move", listOf(Pair(0,2),Pair(-1,3),Pair(1,2),Pair(-1,-2)),8),
                        cooldown = 2,
                        price = 3,
                        id = 8,
                        image_resource = R.drawable.item_image_legs
                ),
                9 to Item(
                        name = "Knight Shield",
                        stat_requirement = StatRequirement(strength_range = 5..10,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 3,
                        ability = Ability("attack", listOf(Pair(0,1),Pair(1,1),Pair(-1,0),Pair(1,0),Pair(-1,1)),5),
                        cooldown = 2,
                        price = 2,
                        id = 9,
                        image_resource = R.drawable.item_image_shield
                ),
                10 to Item(
                        name = "Great Helm",
                        stat_requirement = StatRequirement(strength_range = 5..8,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 0,
                        ability = Ability("move", listOf(Pair(-1,-3),Pair(0,-3),Pair(1,2),Pair(-1,2)),3),
                        cooldown = 1,
                        price = 7,
                        id = 10,
                        image_resource = R.drawable.item_image_helmet
                ),
                11 to Item(
                        name = "Duelist Buckler",
                        stat_requirement = StatRequirement(strength_range = 5..10,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 3,
                        ability = Ability("attack", listOf(Pair(0,1),Pair(1,0),Pair(1,2),Pair(-1,2)),8),
                        cooldown = 1,
                        price = 2,
                        id = 11,
                        image_resource = R.drawable.item_image_shield
                ),
                12 to Item(
                        name = "Chainmail",
                        stat_requirement = StatRequirement(strength_range = 5..8,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 1,
                        ability = Ability("move", listOf(Pair(-1,-1),Pair(-1,-2),Pair(1,2),Pair(-3,1),Pair(1,1),Pair(3,1)), 2),
                        cooldown = 3,
                        price = 7,
                        id = 12,
                        image_resource = R.drawable.item_image_shoulders
                ),
                13 to Item(
                        name = "Plated Greaves",
                        stat_requirement = StatRequirement(strength_range = 6..10,dexterity_range = 2..6,intelligence_range = 1..5),
                        equipment_slot = 2,
                        ability = Ability("move", listOf(Pair(1,3),Pair(-1,3),Pair(1,-3),Pair(-1,-3)),8),
                        cooldown = 1,
                        price = 4,
                        id = 13,
                        image_resource = R.drawable.item_image_legs
                ),
                14 to Item(
                        name = "Sluggish Shield",
                        stat_requirement = StatRequirement(strength_range = 3..8,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 3,
                        ability = Ability("attack", listOf(Pair(0,1),Pair(1,0),Pair(-1,0)),3),
                        cooldown = 3,
                        price = 0,
                        id = 14,
                        image_resource = R.drawable.item_image_shield

                ),
                15 to Item(
                        name = "Green Sword",
                        stat_requirement = StatRequirement(strength_range = 3..10,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 4,
                        ability = Ability(type = "attack", relative_pairs = listOf(Pair(1,1),Pair(-1,1),Pair(0,-1),Pair(0,1)),speed = 4),
                        cooldown = 3,
                        price = 0,
                        id = 15,
                        image_resource = R.drawable.item_image_sword
                ),
                16 to Item(
                        name = "Red Sword",
                        stat_requirement = StatRequirement(strength_range = 5..10,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 4,
                        ability = Ability("attack", listOf(Pair(0,1),Pair(0,3),Pair(1,2),Pair(-1,2)),8),
                        cooldown = 1,
                        price = 6,
                        id = 16,
                        image_resource = R.drawable.item_image_sword
                ),
                17 to Item(
                        name = "Mage Staff",
                        stat_requirement = StatRequirement(strength_range = 5..8,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 4,
                        ability = Ability("attack", listOf(Pair(0,3),Pair(1,2),Pair(-1,2),Pair(-1,3),Pair(1,3)),3),
                        cooldown = 3,
                        price = 5,
                        id = 17,
                        image_resource = R.drawable.item_image_sword
                ),
                18 to Item(
                        name = "Fancy Pants",
                        stat_requirement = StatRequirement(strength_range = 5..10,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 2,
                        ability = Ability("move", listOf(Pair(0,1),Pair(0,3),Pair(1,2),Pair(-1,2)),3),
                        cooldown = 2,
                        price = 9,
                        id = 18,
                        image_resource = R.drawable.item_image_legs
                ),
                19 to Item(
                        name = "Gold Pantaloons",
                        stat_requirement = StatRequirement(strength_range = 5..8,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 2,
                        ability = Ability("move", listOf(Pair(0,2),Pair(-1,3),Pair(1,2),Pair(-1,-2)),8),
                        cooldown = 2,
                        price = 3,
                        id = 19,
                        image_resource = R.drawable.item_image_legs
                ),
                20 to Item(
                        name = "Fire Shield",
                        stat_requirement = StatRequirement(strength_range = 5..10,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 3,
                        ability = Ability("attack", listOf(Pair(0,1),Pair(1,1),Pair(-1,0),Pair(1,0),Pair(-1,1)),5),
                        cooldown = 2,
                        price = 2,
                        id = 20,
                        image_resource = R.drawable.item_image_shield
                ),
                21 to Item(
                        name = "War Helm",
                        stat_requirement = StatRequirement(strength_range = 5..8,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 0,
                        ability = Ability("move", listOf(Pair(-1,-3),Pair(0,-3),Pair(1,2),Pair(-1,2)),3),
                        cooldown = 1,
                        price = 7,
                        id = 21,
                        image_resource = R.drawable.item_image_helmet
                ),
                22 to Item(
                        name = "Iron Buckler",
                        stat_requirement = StatRequirement(strength_range = 5..10,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 3,
                        ability = Ability("attack", listOf(Pair(0,1),Pair(1,0),Pair(1,2),Pair(-1,2)),8),
                        cooldown = 1,
                        price = 2,
                        id = 22,
                        image_resource = R.drawable.item_image_shield
                ),
                23 to Item(
                        name = "Warriors Mail",
                        stat_requirement = StatRequirement(strength_range = 5..8,dexterity_range = 2..6,intelligence_range = 2..6),
                        equipment_slot = 1,
                        ability = Ability("move", listOf(Pair(-1,-1),Pair(-1,-2),Pair(1,2),Pair(-3,1),Pair(1,1),Pair(3,1)), 2),
                        cooldown = 3,
                        price = 7,
                        id = 23,
                        image_resource = R.drawable.item_image_shoulders
                ),
                24 to Item(
                        name = "Agile Boots",
                        stat_requirement = StatRequirement(strength_range = 6..10,dexterity_range = 2..6,intelligence_range = 1..5),
                        equipment_slot = 2,
                        ability = Ability("move", listOf(Pair(1,3),Pair(-1,3),Pair(1,-3),Pair(-1,-3)),8),
                        cooldown = 1,
                        price = 4,
                        id = 24,
                        image_resource = R.drawable.item_image_legs
                )
        )
}

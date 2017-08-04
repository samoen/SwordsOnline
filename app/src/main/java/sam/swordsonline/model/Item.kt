package sam.swordsonline.model

import sam.swordsonline.R

data class Item(var name: String = "default item", var stat_requirement: StatRequirement = StatRequirement(), var equipment_slot: Int = 0, var ability: Ability = Ability(), var price: Int = 0, var cooldown: Int = 1, var image_resource: Int = R.drawable.item_image_sword)
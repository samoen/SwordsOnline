package sam.swordsonline.model

data class StatRequirement(var strength_range: IntRange = 0..11, var dexterity_range: IntRange = 0..11, var intelligence_range: IntRange = 0..11)
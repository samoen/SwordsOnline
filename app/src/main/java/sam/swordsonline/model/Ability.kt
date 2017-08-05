package sam.swordsonline.model

/**
 * Created by Sam on 5/08/2017.
 */
data class Ability(var type: String = "default ability", var relative_pairs: List<Pair<Int, Int>> = listOf(), var speed: Int = 0)
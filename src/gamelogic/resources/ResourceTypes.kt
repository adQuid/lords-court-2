package gamelogic.resources

object ResourceTypes {

    //used by Territory Module
    val ARABLE_LAND_NAME = "arable_land"
    val POPULATION_NAME = "pop"
    val SEEDS_NAME = "seeds"
    val FLOUR_NAME = "flour"
    val BREAD_NAME = "bread"

    //used by Economics Module
    val GOLD_NAME = "gold"
    val FISH_NAME = "fish"

    val tradableTypes = listOf(GOLD_NAME, FISH_NAME)
}
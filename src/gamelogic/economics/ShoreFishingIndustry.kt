package gamelogic.economics

import game.Game
import gamelogic.resources.ResourceTypes
import gamelogic.territory.Territory
import kotlin.math.min

object ShoreFishingIndustry: Industry {
    override fun run(territory: Territory, game: Game, labor: Int): Int {
        val laborToUse = min(labor, 0)

        territory.resources.add(ResourceTypes.FISH_NAME, laborToUse)

        return labor - laborToUse
    }
}
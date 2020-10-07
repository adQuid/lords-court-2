package gamelogic.economics

import game.Game
import gamelogic.territory.Territory

object ConstructionIndustry: Industry {
    override fun run(territory: Territory, game: Game, labor: Int): Int {

        territory.constructions.forEach { construction ->

        }

        return labor //for the time being, no labor gets used
    }
}
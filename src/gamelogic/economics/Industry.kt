package gamelogic.economics

import game.Game
import gamelogic.territory.Territory

interface Industry {

    abstract fun run(territory: Territory, game: Game, labor: Int): Int

}
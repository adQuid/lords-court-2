package gamelogic.territory

import game.Game
import game.Title
import game.titlemaker.TitleFactory

object TerritoryTitleFactory: TitleFactory {
    override fun titleFromSaveString(saveString: Map<String, Any>, game: Game): Title? {
        return null //No titles in this module for now
    }
}
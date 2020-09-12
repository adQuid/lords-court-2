package gamelogic.economics

import game.Game
import game.Title
import game.titlemaker.TitleFactory

class EconomicsTitleFactory:TitleFactory {
    override fun titleFromSaveString(saveString: Map<String, Any>, game: Game): Title? {
        return null //No titles in this module for now
    }


}
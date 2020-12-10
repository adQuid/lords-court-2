package gamelogic.petitioners

import game.Game
import game.Title
import game.titlemaker.TitleFactory

object PetitionersTitleFactory: TitleFactory {
    override fun titleFromSaveString(saveString: Map<String, Any>, game: Game): Title? {
        return null
    }
}
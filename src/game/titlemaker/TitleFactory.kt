package game.titlemaker

import game.Game
import game.Title

interface TitleFactory {

    fun titleFromSaveString(saveString: Map<String, Any>, game: Game): Title?
}
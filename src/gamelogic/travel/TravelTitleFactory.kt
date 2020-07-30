package gamelogic.travel

import game.Game
import game.Title
import game.titlemaker.TitleFactory

object TravelTitleFactory: TitleFactory {

    val typeMap: HashMap<String, (map: Map<String, Any>, game: Game) -> Title> = hashMapOf(
    )

    override fun titleFromSaveString(saveString: Map<String, Any>, game: Game): Title? {
        return null
    }
}
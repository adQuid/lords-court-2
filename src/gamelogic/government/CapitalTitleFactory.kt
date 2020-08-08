package gamelogic.government

import game.Game
import game.Title
import game.titlemaker.CookieWorldTitleFactory
import game.titlemaker.TitleFactory

object CapitalTitleFactory: TitleFactory {

    val typeMap: HashMap<String, (map: Map<String, Any>, game: Game) -> Title> = hashMapOf(
        "Count" to {map, game -> Count(map, game)},
        "King" to {map, game -> King(map, game)}
    )

    override fun titleFromSaveString(saveString: Map<String, Any>, game: Game): Title? {
        if(typeMap.containsKey(saveString[CookieWorldTitleFactory.TYPE_NAME])){
            return typeMap[saveString[CookieWorldTitleFactory.TYPE_NAME]]!!(saveString, game)
        }else{
            return null
        }
    }
}
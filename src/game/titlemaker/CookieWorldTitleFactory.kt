package game.titlemaker

import game.Game
import game.Title
import game.titles.Baker
import game.titles.Milkman

object CookieWorldTitleFactory: TitleFactory {

    val TYPE_NAME = "TYPE"
    val typeMap: HashMap<String, (map: Map<String, Any>) -> Title> = hashMapOf(
        "Baker" to {map -> Baker(map)},
        "Milkman" to {map -> Milkman(map)}
    )

    override fun titleFromSaveString(saveString: Map<String, Any>, game: Game): Title?{
        if(typeMap.containsKey(saveString[TYPE_NAME])){
            return typeMap[saveString[TYPE_NAME]]!!(saveString)
        } else {
            return null
        }
    }

    fun makeBakerTitle(name: String): Title {
        return Baker(name)
    }

    fun makeMilkmanTitle(): Title {
        return Milkman()
    }

}
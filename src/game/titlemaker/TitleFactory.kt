package game.titlemaker

import game.Title
import game.titles.Count
import game.titles.Milkman

object TitleFactory {

    val TYPE_NAME = "TYPE"
    val typeMap: HashMap<String, (map: Map<String, Any>) -> Title> = hashMapOf(
        "Count" to {map -> Count(map)},
        "Milkman" to {map -> Milkman()}
    )

    fun titleFromSaveString(saveString: Map<String, Any>): Title{
        return typeMap[saveString[TYPE_NAME]]!!(saveString)
    }

    fun makeCountTitle(name: String): Title {
        return Count(name)
    }

    fun makeMilkmanTitle(): Title {
        return Milkman()
    }

}
package game.titlemaker

import game.Title
import game.titles.Count

object TitleFactory {

    val TYPE_NAME = "TYPE"
    val typeMap: HashMap<String, (map: Map<String, Any>) -> Title> = hashMapOf(
        "Count" to {map -> Count(map)}
    )

    fun titleFromSaveString(saveString: Map<String, Any>): Title{
        return typeMap[saveString[TYPE_NAME]]!!(saveString)
    }

    fun makeCountTitle(name: String): Title {
        return Count(name)
    }


}
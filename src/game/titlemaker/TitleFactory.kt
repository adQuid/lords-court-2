package game.titlemaker

import game.Title
import game.titles.Baker
import game.titles.Milkman

object TitleFactory {

    val TYPE_NAME = "TYPE"
    val typeMap: HashMap<String, (map: Map<String, Any>) -> Title> = hashMapOf(
        "Count" to {map -> Baker(map)},
        "Milkman" to {map -> Milkman(map)}
    )

    fun titleFromSaveString(saveString: Map<String, Any>): Title{
        return typeMap[saveString[TYPE_NAME]]!!(saveString)
    }

    fun makeBakerTitle(name: String): Title {
        return Baker(name)
    }

    fun makeMilkmanTitle(): Title {
        return Milkman()
    }

}
package game.culture

import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import ui.commoncomponents.PrettyPrintable

class Topic: PrettyPrintable {
    val group: String
    val name: String
    val description: String

    constructor(group: String, name: String, description: String) {
        this.group = group
        this.name = name
        this.description = description
    }

    constructor(saveString: Map<String, Any>): this(saveString["group"] as String, saveString["name"] as String, saveString["description"] as String)

    fun saveString(): Map<String, Any>{
        return mapOf(
            "group" to group,
            "name" to name,
            "description" to description
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Topic

        if (group != other.group) return false
        if (name != other.name) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = group.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }

    override fun prettyPrint(context: ShortStateGame, perspective: ShortStateCharacter): String {
        return name
    }
}
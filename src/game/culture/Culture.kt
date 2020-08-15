package game.culture

class Culture {

    val name: String

    val topics: MutableMap<String, String>

    constructor(name: String, topics: MutableMap<String, String>){
        this.name = name
        this.topics = topics
    }

    constructor(other: Culture){
        this.name = other.name

        this.topics = HashMap(other.topics)
    }

    constructor(saveString: Map<String, Any>){
        name = saveString["name"] as String
        topics = (saveString["topics"] as Map<String, String>).toMutableMap()
    }

    fun saveString(): Map<String, Any>{
        return mapOf(
            "name" to name,
            "topics" to topics
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Culture

        if (name != other.name) return false
        if (topics != other.topics) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + topics.hashCode()
        return result
    }
}
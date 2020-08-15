package game.culture

class Culture {

    val name: String

    val topics: MutableSet<Topic>

    constructor(name: String, topics: MutableSet<Topic>){
        this.name = name
        this.topics = topics
    }

    constructor(other: Culture){
        this.name = other.name

        this.topics = HashSet(other.topics)
    }

    constructor(saveString: Map<String, Any>){
        name = saveString["name"] as String
        topics = (saveString["topics"] as List<Map<String, Any>>).map{Topic(it)}.toMutableSet()
    }

    fun saveString(): Map<String, Any>{
        return mapOf(
            "name" to name,
            "topics" to topics.map { it.saveString() }
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
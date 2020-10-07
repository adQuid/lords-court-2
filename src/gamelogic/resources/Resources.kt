package gamelogic.resources

import kotlin.math.roundToInt

class Resources {

    val resources: MutableMap<String, Int>

    constructor(){
        resources = mutableMapOf()
    }

    constructor(other: Resources){
        resources = other.resources.toMutableMap()
    }

    constructor(saveString: Map<String, Any>){
        resources = (saveString as Map<String, Int>).toMutableMap()
    }

    fun saveString(): Map<String, Any> {
        return resources
    }

    operator fun get(name: String): Int {
        return resources.getOrDefault(name, 0)
    }

    fun set(name: String, value: Int){
        resources[name] = value
    }

    fun add(name: String, value: Int){
        if(!resources.containsKey(name)){
            resources[name] = 0
        }
        resources[name] = resources[name]!! + value
    }

    fun addAll(other: Resources){
        other.resources.forEach{add(it.key, it.value)}
    }

    fun subtractAll(other: Resources){
        other.resources.forEach{add(it.key, -it.value)}
    }

    fun multiply(name: String, value: Double){
        if(!resources.containsKey(name)){
            resources[name] = 0
        }
        resources[name] = (resources[name]!! * value).roundToInt()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Resources

        if (resources != other.resources) return false

        return true
    }

    override fun hashCode(): Int {
        return resources.hashCode()
    }


}
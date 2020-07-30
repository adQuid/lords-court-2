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

    fun get(name: String): Int {
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

    fun multiply(name: String, value: Double){
        if(!resources.containsKey(name)){
            resources[name] = 0
        }
        resources[name] = (resources[name]!! * value).roundToInt()
    }
}
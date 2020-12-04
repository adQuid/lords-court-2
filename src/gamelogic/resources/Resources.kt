package gamelogic.resources

import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import ui.Describable
import ui.commoncomponents.PrettyPrintable
import kotlin.math.roundToInt

class Resources: Describable {

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

    fun plus(other: Resources): Resources{
        val retval = Resources(this)
        retval.addAll(other)
        return retval
    }

    fun minus(other:Resources): Resources{
        val retval = Resources(this)
        retval.subtractAll(other)
        return retval
    }

    fun greaterThanOrEqualTo(other: Resources): Boolean{
        return this.minus(other).resources.filter { it.value < 0 }.isEmpty()
    }

    fun negative(): Resources{
        return Resources(resources.mapValues{ it.value * -1 })
    }

    override fun tooltip(perspective: ShortStateCharacter): String {
        return resources.map { "${it.value} ${it.key}" }.joinToString (",")
    }

    override fun description(): String {
        return resources.map { "${it.value} ${it.key}" }.joinToString (",")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Resources

        resources.forEach {
            if(get(it.key) != other.get(it.key)){
                return false
            }
        }

        return true
    }

    override fun hashCode(): Int {
        return resources.hashCode()
    }


}
package aibrain

class Score {

    private val components = mutableListOf<ScoreComponent>()

    constructor()

    constructor(components: List<ScoreComponent>){
        components.forEach {
            add(it.name, it.description, it.value)
        }
    }

    fun add(name: String, description: (value: Double) -> String, value: Double){
        if(components.filter{it.name == name}.isNotEmpty()){
            components.filter{it.name == name}.first().value += value
        } else {
            components.add(ScoreComponent(name, description, value))
        }
    }

    fun subtract(name: String, description: (value: Double) -> String, value: Double){
        if(components.filter{it.name == name}.isNotEmpty()){
            val component = components.filter{it.name == name}.first()
            component.value -= value
            if(component.value == 0.0){
                components.remove(component)
            }
        } else {
            components.add(ScoreComponent(name, description, value))
        }
    }

    fun components(): List<ScoreComponent>{
        return components.toList()
    }

    fun value(): Double{
        return components().sumByDouble { it.value }
    }

    fun plus(other: Score): Score{
        val retval = Score(components())
        other.components().forEach { retval.add(it.name, it.description, it.value) }
        return retval
    }

    fun minus(other: Score): Score{
        val retval = Score(components())
        other.components().forEach { retval.subtract(it.name, it.description, it.value) }
        return retval
    }

    fun dividedBy(denominator: Double): Score{
        val retval = Score(components())
        retval.components.forEach{it.value /= denominator}
        return retval
    }

    class ScoreComponent{
        val name: String
        val description: (value: Double) -> String
        var value: Double

        constructor(name: String, description: (value: Double) -> String, value: Double){
            this.name = name
            this.description = description
            this.value = value
        }

        override fun toString(): String {
            return description(value)
        }

        fun description(): String {
            return description(value)
        }
    }
}
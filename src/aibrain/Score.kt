package aibrain

class Score {

    private val components = mutableListOf<ScoreComponent>()

    constructor()

    constructor(components: List<ScoreComponent>){
        components.forEach {
            add(it.name, it.description, it.value)
        }
    }

    fun add(name: String, description: String, value: Double){
        if(components.filter{it.name == name && it.description == description}.isNotEmpty()){
            components.filter{it.name == name && it.description == description}.first().value += value
        } else {
            components.add(ScoreComponent(name, description, value))
        }
    }

    fun subtract(name: String, description: String, value: Double){
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

    class ScoreComponent{
        val name: String
        val description: String
        var value: Double

        constructor(name: String, description: String, value: Double){
            this.name = name
            this.description = description
            this.value = value
        }

        override fun toString(): String {
            return description
        }
    }
}
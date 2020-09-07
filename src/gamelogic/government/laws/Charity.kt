package gamelogic.government.laws

import gamelogic.government.Law
import gamelogic.territory.Territory

class Charity: Law {

    companion object{
        val type = "charity"

        val ON_NEED_NAME = "onneed"
    }

    var onNeed = false

    constructor(onNeed: Boolean): super(Charity.type){
        this.onNeed = onNeed
    }

    constructor(saveString: Map<String, Any>): super(saveString["type"] as String){
        onNeed = saveString[ON_NEED_NAME] as Boolean
    }

    override fun specialSaveString(): Map<String, Any>{
        return mapOf(ON_NEED_NAME to onNeed)
    }

    override fun apply() {

        if(onNeed){
            val foodToEat = capital!!.territory!!.foodToEatNextTurn()
            val totalFood = foodToEat.resources.entries.sumBy { it.value }
            if(totalFood < capital!!.territory!!.resources.get(Territory.POPULATION_NAME)){
                val foodToGive = Territory.extractFood(capital!!.resources, capital!!.territory!!.resources.get(Territory.POPULATION_NAME) - totalFood)
                capital!!.resources.subtractAll(foodToGive)
                capital!!.territory!!.resources.addAll(foodToGive)
            }
        }

    }


}
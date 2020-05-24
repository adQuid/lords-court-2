package gamelogicmodules.territory

class Crop {

    val QUANTITY_NAME = "q"
    var quantity: Int
    val PLANTING_TIME_NAME = "t"
    val plantingTime: Int

    constructor(quantity: Int, plantingTime: Int){
        this.quantity = quantity
        this.plantingTime = plantingTime
    }

    constructor(other: Crop){
        this.quantity = other.quantity
        this.plantingTime = other.plantingTime
    }
    
    constructor(saveString: Map<String, Any>){
        quantity = saveString[QUANTITY_NAME] as Int
        plantingTime = saveString[PLANTING_TIME_NAME] as Int
    }

    fun saveString(): Map<String,Any>{
        return mapOf(
            QUANTITY_NAME to quantity,
            PLANTING_TIME_NAME to plantingTime
        )
    }

    override fun toString(): String {
        return "$quantity planted on turn $plantingTime"
    }

    fun harvestAge(): Int{
        return 10
    }

    fun yield(): Int {
        return 7
    }

}
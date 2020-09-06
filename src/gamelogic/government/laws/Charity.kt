package gamelogic.government.laws

import gamelogic.government.Capital
import gamelogic.government.Law

class Charity: Law {

    companion object{
        val type = "charity"
    }

    constructor(): super(Charity.type){

    }

    constructor(saveString: Map<String, Any>): super(saveString["type"] as String){

    }

    override fun specialSaveString(): Map<String, Any>{
        return mapOf()
    }

    override fun apply() {

    }


}
package gamelogic.government.laws

import gamelogic.government.Law

object GlobalLawFactory {

    val allLaws = mapOf<String, (saveString: Map<String, Any>) -> Law>(Charity.type to { _ -> Charity()})

    fun lawFromSaveString(saveString: Map<String, Any>): Law {
        return allLaws[saveString["type"] as String]!!(saveString)
    }

}
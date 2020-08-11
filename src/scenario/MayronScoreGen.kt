package scenario

import aibrain.Score
import aibrain.SpecialScoreGenerator
import game.Game
import gamelogic.government.Count
import gamelogic.government.GovernmentLogicModule

class MayronScoreGen: SpecialScoreGenerator {
    companion object{
        val typeName: String
            get() = "mayronspecial"
    }

    override fun score(game: Game): Score {
        val retval = Score()

        val mySon = game.playerCharacter()
        val startingCountyTitle = game.titles
            .filter { it is Count && it.capital == (game.moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule).capitals.filter{it.territory!!.name=="Port Fog"}.first()}.first()

        if(mySon.titles.contains(startingCountyTitle)){
            retval.add("land for son", "My son will have lands of his own", 10000.0)
        }
        return retval
    }

    override fun saveString(): Map<String, Any> {
        return mapOf(
            "type" to typeName
        )
    }
}
package gamelogic.government.actionTypes

import game.Game
import game.GameCharacter
import game.action.Action
import gamelogic.economics.Construction
import gamelogic.territory.Territory
import gamelogic.territory.TerritoryLogicModule
import shortstate.ShortStateCharacter

class LaunchConstruction: Action {

    val construction: Construction
    val terID: Int

    constructor(construction: Construction, territory: Int){
        this.construction = construction
        this.terID = territory
    }

    constructor(saveString: Map<String, Any>){
        this.construction = Construction(saveString["construction"] as Map<String, Any>)
        this.terID = saveString["territory"] as Int
    }

    override fun isLegal(game: Game, player: GameCharacter): Boolean {
        return true
    }

    override fun doAction(game: Game, player: GameCharacter) {
        val territory = (game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule).territoryById(terID)

        if(game.resourcesByCharacter(player).greaterThanOrEqualTo(construction.budget)){
            game.addResourceForCharacter(player, construction.budget)
            territory.constructions.add(construction)
        }
    }

    override fun saveString(): Map<String, Any> {
        return mapOf(
            "construction" to construction.saveString(),
            "territory" to terID
        )
    }

    override fun collidesWith(other: Action): Boolean {
        return false
    }

    override fun tooltip(perspective: ShortStateCharacter): String {
        return "Build ${construction.structure.type}"
    }

    override fun description(): String {
        return "Build ${construction.structure.type} using ${construction.budget}"
    }
}
package gamelogic.travel

import aibrain.Plan
import aibrain.Score
import game.*
import gamelogic.government.specialdisplayables.TravelView
import gamelogic.territory.TerritoryLogicModule
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.Button
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.componentfactory.UtilityComponentFactory

class TravelLogicModule: GameLogicModule{
    override val type: String
        get() = "" //To change initializer of created properties use File | Settings | File Templates.

    constructor(): super(listOf(), TravelTitleFactory, listOf(TerritoryLogicModule.type)){

    }

    override fun finishConstruction(game: Game) {

    }

    override fun endTurn(game: Game){
        //do nothing
    }

    override fun locations(): Collection<Location> {
        return listOf()
    }

    override fun specialSaveString(): Map<String, Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun planOptions(
        perspective: GameCharacter,
        importantPlayers: Collection<GameCharacter>
    ): Collection<Plan> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun score(perspective: GameCharacter): Score {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
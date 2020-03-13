package test.fixtures

import aibrain.Deal
import aibrain.FinishedDeal
import game.Effect
import game.Game
import game.GameCharacter
import game.action.Action

class DummyGoodThing: Action.ActionType() {
    override fun doAction(game: Game, player: GameCharacter): List<Effect> {
        return listOf(DummyGoodEffect(1.0))
    }

    override fun saveString(): Map<String, Any> {
        //I'm not testing saving dummy actions
        return mapOf()
    }

    class DummyGoodEffect(override var probability: Double) : Effect() {
        override fun equals(other: Any?): Boolean {
            if(other is DummyGoodEffect){
                return true
            }
            return false
        }

        override fun apply(game: Game) {
            game.players.forEach {
                player -> player.dummyScore++
            }
        }

        override fun describe(): String {
            return "dummy good"
        }

        override fun saveString(): Map<String, Any> {
            //I'm not testing saving dummy effects
            return mapOf()
        }

    }
}

fun goodDeal(game: Game): Deal {
    val map = mutableMapOf<GameCharacter, List<Action>>()
    map[game.players[0]] = listOf(Action(DummyGoodThing()))
    return FinishedDeal(map)
}
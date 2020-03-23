package test.fixtures

import aibrain.Deal
import aibrain.FinishedDeal
import game.Effect
import game.Game
import game.GameCharacter
import game.action.Action
import game.action.actionTypes.WasteTime

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

class DummyBadThing: Action.ActionType() {
    override fun doAction(game: Game, player: GameCharacter): List<Effect> {
        return listOf(DummyBadEffect(1.0))
    }

    override fun saveString(): Map<String, Any> {
        //I'm not testing saving dummy actions
        return mapOf()
    }

    class DummyBadEffect(override var probability: Double) : Effect() {
        override fun equals(other: Any?): Boolean {
            if(other is DummyBadEffect){
                return true
            }
            return false
        }

        override fun apply(game: Game) {
            game.players.forEach {
                    player -> player.dummyScore--
            }
        }

        override fun describe(): String {
            return "dummy bad"
        }

        override fun saveString(): Map<String, Any> {
            //I'm not testing saving dummy effects
            return mapOf()
        }

    }
}

class DummyNeutralThing: Action.ActionType() {
    override fun doAction(game: Game, player: GameCharacter): List<Effect> {
        return listOf()
    }

    override fun saveString(): Map<String, Any> {
        //I'm not testing saving dummy actions
        return mapOf()
    }
}

fun goodDeal(players: List<GameCharacter>): Deal {
    return deal(players, DummyGoodThing())
}

fun badDeal(players: List<GameCharacter>): Deal {
    return deal(players, DummyBadThing())
}

fun neutralDeal(players: List<GameCharacter>): Deal {
    return deal(players, WasteTime()) //TODO: change this to a proper test timewasting action that can save
}

private fun deal(players: List<GameCharacter>, action: Action.ActionType): Deal{
    val map = mutableMapOf<GameCharacter, List<Action>>()
    players.forEach {
        map[it] = listOf(Action(action))
    }
    return FinishedDeal(map)
}
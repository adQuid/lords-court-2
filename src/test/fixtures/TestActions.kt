package test.fixtures

import aibrain.Deal
import aibrain.FinishedDeal
import game.Effect
import game.Game
import game.GameCharacter
import game.action.Action
import gamelogic.cookieworld.actionTypes.WasteTime

class DummyGoodThing: DummyAction() {
    override fun doAction(game: Game, player: GameCharacter): List<Effect> {
        return listOf(DummyGoodEffect(1.0))
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

class DummyOneTimeGoodThing: DummyAction() {
    override fun doAction(game: Game, player: GameCharacter): List<Effect> {
        return listOf(DummyOneTimeGoodEffect(1.0))
    }

    class DummyOneTimeGoodEffect(override var probability: Double) : Effect() {
        override fun equals(other: Any?): Boolean {
            if(other is DummyOneTimeGoodEffect){
                return true
            }
            return false
        }

        override fun apply(game: Game) {
            game.players.forEach {
                player -> if(player.dummyScore == 10.0){
                    player.dummyScore++
                }
            }
        }

        override fun describe(): String {
            return "dummy one-time good"
        }

        override fun saveString(): Map<String, Any> {
            //I'm not testing saving dummy effects
            return mapOf()
        }

    }
}

class DummyBadThing: DummyAction() {
    override fun doAction(game: Game, player: GameCharacter): List<Effect> {
        return listOf(DummyBadEffect(1.0))
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

class DummyNeutralThing: DummyAction() {
    override fun doAction(game: Game, player: GameCharacter): List<Effect> {
        return listOf()
    }
}

fun goodDeal(players: List<GameCharacter>): Deal {
    return deal(players, DummyGoodThing())
}

fun oneTimeGoodDeal(players: List<GameCharacter>): Deal{
    return deal(players, DummyOneTimeGoodThing())
}

fun badDeal(players: List<GameCharacter>): Deal {
    return deal(players, DummyBadThing())
}

fun neutralDeal(players: List<GameCharacter>): Deal {
    return deal(players, WasteTime()) //TODO: change this to a proper test timewasting action that can save
}

private fun deal(players: List<GameCharacter>, action: Action): Deal{
    val map = mutableMapOf<GameCharacter, Set<Action>>()
    players.forEach {
        map[it] = setOf(action)
    }
    return FinishedDeal(map)
}

abstract class DummyAction: Action(){

    override fun saveString(): Map<String, Any> {
        //I'm not testing saving dummy actions
        return mapOf()
    }

    override fun description(): String {
        return "Dummy Neutral Thing"
    }

    override fun collidesWith(other: Action): Boolean {
        return false
    }
}
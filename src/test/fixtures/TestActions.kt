package test.fixtures

import aibrain.Deal
import aibrain.FinishedDeal
import game.Game
import game.GameCharacter
import game.action.Action
import gamelogic.cookieworld.actionTypes.WasteTime
import shortstate.ShortStateCharacter

class DummyGoodThing: DummyAction() {
    override fun doAction(game: Game, player: GameCharacter) {
        game.players.forEach {
                player -> player.dummyScore++
        }
    }

}

class DummyOneTimeGoodThing: DummyAction() {
    override fun doAction(game: Game, player: GameCharacter){
        game.players.forEach {
                player -> if(player.dummyScore == 10.0){
            player.dummyScore++
        }
        }
    }


}

class DummyBadThing: DummyAction() {
    override fun doAction(game: Game, player: GameCharacter){
        game.players.forEach {
                player -> player.dummyScore--
        }
    }

}

class DummyNeutralThing: DummyAction() {
    override fun doAction(game: Game, player: GameCharacter) {
        //do nothing
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

    override fun tooltip(perspective: ShortStateCharacter): String {
        return "Dummy Action"
    }

    override fun description(): String {
        return "Dummy Action, only intended for tests"
    }

    override fun isLegal(game: Game, player: GameCharacter): Boolean {
        return true
    }

    override fun collidesWith(other: Action): Boolean {
        return false
    }
}
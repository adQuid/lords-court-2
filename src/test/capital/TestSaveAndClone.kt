package test.capital

import game.Game
import gamelogic.government.Capital
import gamelogic.government.GovernmentLogicModule
import gamelogic.government.actionTypes.SetTaxRate
import gamelogic.territory.Territory
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.TerritoryMap
import org.junit.Test
import game.GameCharacter
import gamelogic.government.Kingdom

fun soloGovernmentTestGame(): Game {
    val territories = TerritoryMap("test")
    territories.territories.add(Territory(territories.nextId++,"Placeburg",0,0))

    val govLogic = GovernmentLogicModule(listOf(Capital(territories.territories[0])),
        listOf(Kingdom("Test Kingdom", territories.territories)))
    val game = Game(listOf(TerritoryLogicModule(territories),
        govLogic
    ))
    val player = GameCharacter("name", "image", true, game.locations().first(), game)
    game.applyTitleToCharacter(govLogic.capitals.first().generateCountTitle(), player)
    game.applyTitleToCharacter(govLogic.kingdoms.first().generateKingTitle(), player)
    player.privateResources.set("some resource", 10)
    game.players.add(player)

    game.actionsByPlayer.put(game.players[0], mutableListOf(SetTaxRate(territories.territories[0].id, 0.2)))
    return game
}

fun twoCapitalTestGame(): Game {
    val territories = TerritoryMap("test")
    territories.territories.add(Territory(territories.nextId++,"Port Fog",0,0))
    territories.territories.add(Territory(territories.nextId++,"Worthford",0,0))
    territories.territories.add(Territory(territories.nextId++,"Sarsburg",0,0))
    territories.territories.add(Territory(territories.nextId++,"Mt. Mist",0,0))
    territories.territories.add(Territory(territories.nextId++,"Craytoyl",0,0))
    territories.territories.add(Territory(territories.nextId++,"Some other",0,0))
    territories.territories.add(Territory(territories.nextId++,"Placeland",0,0))
    territories.territories.add(Territory(territories.nextId++,"Near",0,0))
    territories.territories.add(Territory(territories.nextId++,"Far",0,0))

    val territoryLogic = TerritoryLogicModule(territories)

    val govLogic = GovernmentLogicModule(territories.territories.map{Capital(it)},
        listOf(Kingdom("Test Kingdom", listOf(
            territoryLogic.territories().first { it.name == "Port Fog" },
            territoryLogic.territories().first { it.name == "Worthford" },
            territoryLogic.territories().first { it.name == "Sarsburg" },
            territoryLogic.territories().first { it.name == "Mt. Mist" },
            territoryLogic.territories().first { it.name == "Craytoyl" }
        ))))
    val game = Game(listOf(territoryLogic,
        govLogic
    ))
    val player = GameCharacter("name", "image", true, game.locations().first(), game)
    govLogic.capitals.filter{it.territory!!.name == "Port Fog" || it.territory!!.name == "Sarsburg"}.forEach { game.applyTitleToCharacter(it.generateCountTitle(), player) }
    game.applyTitleToCharacter(govLogic.kingdoms.first().generateKingTitle(), player)
    player.privateResources.set("some resource", 10)
    game.players.add(player)

    game.actionsByPlayer.put(game.players[0], mutableListOf(SetTaxRate(territories.territories[0].id, 0.2)))
    return game
}

class TestSaveAndClone {

    @Test
    fun test_clone(){
        val game = soloGovernmentTestGame()
        val game2 = Game(game)

        assert(game == game2)
        game2.endTurn() //just in case this makes something crash
    }

    @Test
    fun test_save(){
        val game = soloGovernmentTestGame()
        val game2 = Game(game.saveString())

        assert(game == game2)
        assert(game.players[0].npc == game2.players[0].npc)
        assert(game.players[0].privateResources.resources == game2.players[0].privateResources.resources)
        game2.endTurn() //just in case this makes something crash
    }

}
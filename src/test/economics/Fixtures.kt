package test.economics

import game.Game
import gamelogic.government.Capital
import gamelogic.government.GovernmentLogicModule
import gamelogic.government.Kingdom
import gamelogic.government.actionTypes.SetTaxRate
import gamelogic.government.laws.Charity
import gamelogic.territory.Territory
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.TerritoryMap
import game.GameCharacter
import gamelogic.economics.EconomicsLogicModule

object Fixtures {
    fun soloEconomicsTestGame(): Game{
        val territories = TerritoryMap("test")
        territories.territories.add(Territory(territories.nextId++,"Placeburg",0,0))

        val govLogic = GovernmentLogicModule(listOf(Capital(territories.territories[0])),
            listOf(Kingdom("Test Kingdom", territories.territories)))

        govLogic.capitals.first().enactLaw(Charity(false))

        val economics = EconomicsLogicModule()

        val game = Game(listOf(
            TerritoryLogicModule(territories),
            govLogic,
            economics
        ))
        val player = GameCharacter("name", "image", true, game.locations().first(), game)
        game.applyTitleToCharacter(govLogic.capitals.first().generateCountTitle(), player)
        game.applyTitleToCharacter(govLogic.kingdoms.first().generateKingTitle(), player)
        player.privateResources.set("some resource", 10)
        game.players.add(player)

        game.actionsByPlayer.put(game.players[0], mutableListOf(SetTaxRate(territories.territories[0].id, 0.2)))
        return game
    }

}
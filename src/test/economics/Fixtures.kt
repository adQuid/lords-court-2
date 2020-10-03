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
import gamelogic.territory.mapobjects.StructureType

object Fixtures {
    fun soloEconomicsTestGame(): Game{
        val territories = TerritoryMap("test")
        territories.territories.add(Territory(territories.nextId++,"Placeburg",0,0))

        val govLogic = GovernmentLogicModule(listOf(Capital(territories.territories[0])),
            listOf(Kingdom("Test Kingdom", territories.territories)))

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

    fun setupStructureTypes(){
        StructureType.allTypes = listOf(
            StructureType(mapOf(
                "name" to "building that does nothing",
                "manufactoring" to listOf<Map<String, Any>>()
            )),
            StructureType(mapOf(
                "name" to "building that makes flour from just labor, but only once",
                "manufactoring" to listOf<Map<String, Any>>(
                    mapOf(
                        "thruput" to 1,
                        "input" to mapOf("labor" to 1),
                        "output" to mapOf("flour" to 1)
                    )
                )
            ))
        )
    }

}
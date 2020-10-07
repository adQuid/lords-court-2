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
import gamelogic.economics.Construction
import gamelogic.economics.EconomicsLogicModule
import gamelogic.resources.ResourceTypes
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

    fun gameWithConstruction(construction: Construction): Game{
        val game = soloEconomicsTestGame()

        val territories = game.moduleOfType(TerritoryLogicModule.type) as TerritoryLogicModule
        val territory = territories.territories().first()

        territory.constructions.add(construction)

        return game
    }

    fun setupStructureTypes(){
        StructureType.allTypes = listOf(
            StructureType(mapOf(
                "name" to "building that does nothing",
                "cost" to mapOf<String, Any>(),
                "construction throughput" to 0,
                "manufactoring" to listOf<Map<String, Any>>()
            )),
            StructureType(mapOf(
                "name" to "building that needs 10 labor",
                "cost" to mapOf<String, Any>(EconomicsLogicModule.LABOR_NAME to 10),
                "construction throughput" to 10,
                "manufactoring" to listOf<Map<String, Any>>()
            )),
            StructureType(mapOf(
                "name" to "building that makes flour from just labor, but only once",
                "cost" to mapOf<String, Any>(),
                "construction throughput" to 0,
                "manufactoring" to listOf(
                    mapOf(
                        "thruput" to 1,
                        "input" to mapOf("labor" to 1),
                        "output" to mapOf("flour" to 1)
                    )
                )
            )),
            StructureType(mapOf(
                "name" to "watermill",
                "cost" to mapOf<String, Any>(),
                "construction throughput" to 0,
                "manufactoring" to listOf(
                    mapOf(
                        "thruput" to 1,
                        "input" to mapOf("labor" to 1, ResourceTypes.SEEDS_NAME to 4),
                        "output" to mapOf("flour" to 4)
                    )
                )
            ))
        )
    }

}
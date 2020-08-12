package game

import aibrain.FinishedDeal
import gamelogic.cookieworld.CookieWorld
import gamelogic.government.Capital
import gamelogic.government.GovernmentLogicModule
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.TerritoryMap
import game.titlemaker.CookieWorldTitleFactory
import gamelogic.government.Kingdom
import gamelogic.government.actionTypes.GiveTerritory
import gamelogic.government.actionTypes.SetTaxRate
import gamelogic.playerresources.GiveResource
import gamelogic.playerresources.PlayerResourceModule
import gamelogic.playerresources.PlayerResourceTypes
import gamelogic.territory.Territory
import scenario.*
import ui.specialdisplayables.worldgen.WorldEditorMainMenu
import java.util.*

object GameSetup {

    fun setupCookieworld(): Game{
        val cookieWorld = CookieWorld()
        val game = Game(listOf(cookieWorld))

        val PC = GameCharacter("Melkar the Magnificant", "assets/general/conversation frame.png", false, cookieWorld.locations.first(), game)
        game.applyTitleToCharacter(CookieWorldTitleFactory.makeBakerTitle("Cookies"), PC)
        game.addPlayer(PC)

        val NPC = GameCharacter("Frip", "assets/portraits/faceman.png", true, cookieWorld.locations.first(), game)
        NPC.titles.add(CookieWorldTitleFactory.makeMilkmanTitle())
        game.addPlayer(NPC)

        return game
    }

    fun setupAgricultureGame(): Game{
        val territories = TerritoryMap.fromMap(WorldEditorMainMenu.mapName)
        val territoryLogic = TerritoryLogicModule(territories)
        val danswada = Kingdom("Dansawda", listOf(
            territoryLogic.territories().first { it.name == "Port Fog" },
            territoryLogic.territories().first { it.name == "Worthford" },
            territoryLogic.territories().first { it.name == "Sarsburg" },
            territoryLogic.territories().first { it.name == "Mt. Mist" },
            territoryLogic.territories().first { it.name == "Craytoyl" }
        ))
        val capitals = GovernmentLogicModule(territoryLogic.map.territories.map { Capital(it) }, listOf(danswada))
        val game = Game(listOf(PlayerResourceModule(), territoryLogic, capitals), "tutorial")

        val pcCapital = capitals.capitals.first()
        val PC = GameCharacter("Melkar the Magnificant", "assets/general/conversation frame.png", false, pcCapital.location, game)
        PC.specialLines.add(talkToDadTrigger1)
        PC.specialLines.add(talkToDadTrigger2)
        PC.specialLines.add(talkToDadTrigger3)
        game.addPlayer(PC)

        val advisor = GameCharacter("Kaireth", "assets/portraits/Kaireth.png", true, pcCapital.location, game)
        advisor.specialLines.add(adviceOnBadFishTrade)
        advisor.specialLines.add(adviseToGetFish)
        advisor.specialLines.add(chideForBadDeal)
        game.addPlayer(advisor)



        val dad = GameCharacter("Mayren", "assets/portraits/King.png", true, pcCapital.location, game)
        dad.resources.set(PlayerResourceTypes.GOLD_NAME, 100)
        dad.specialScoreGenerators.add(MayronScoreGen())
        dad.specialLines.add(talkToDadTrigger4)


        //dad.writs.add(writ)

        game.applyTitleToCharacter(capitals.capitalOf(territoryLogic.territories().first { it.name == "Worthford" }).generateCountTitle(), dad)
        game.applyTitleToCharacter(pcCapital.generateCountTitle(), dad)
        game.applyTitleToCharacter(danswada.generateKingTitle(), dad)
        game.addPlayer(dad)

        val names = Stack<String>()
        names.addAll(listOf("Faceperson", "De Puce", "Countington", "Fred", "Fredmark", "Billybob", "Tim", "Starwin", "Artyom", "Elsvin", "Krolm", "Ashta"))
        capitals.capitals.forEach {
            if(capitals.countOfCaptial(it.terId) == null){
                val NPC = GameCharacter("Lord "+names.pop(), "assets/portraits/faceman.png", true,it.location,game)
                game.addPlayer(NPC)
                game.applyTitleToCharacter(it.generateCountTitle(), NPC)
            }
        }

        game.endTurn()
        return game
    }
}
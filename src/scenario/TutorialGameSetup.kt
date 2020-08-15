package scenario

import game.Game
import game.culture.Culture
import gamelogic.government.Capital
import gamelogic.government.GovernmentLogicModule
import gamelogic.government.Kingdom
import gamelogic.playerresources.PlayerResourceModule
import gamelogic.playerresources.PlayerResourceTypes
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.TerritoryMap
import ui.specialdisplayables.worldgen.WorldEditorMainMenu
import game.GameCharacter
import java.util.*

object TutorialGameSetup {

    fun setupAgricultureGame(): Game {
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

        val tutorialCulture = Culture("tutorial", mutableMapOf("Writ" to "Writs are made in the workroom"))
        game.cultures.add(tutorialCulture)

        val pcCapital = capitals.capitals.first()
        val PC = GameCharacter("Melkar", "assets/general/conversation frame.png", false, pcCapital.location, game)
        PC.specialLines.add(talkToDadTrigger1)
        PC.specialLines.add(talkToDadTrigger2)
        PC.specialLines.add(talkToDadTrigger3)
        game.addPlayer(PC)

        val advisor = GameCharacter("Kaireth", "assets/portraits/Kaireth.png", true, pcCapital.location, game)
        advisor.specialLines.add(adviseToTalkToDad)
        advisor.specialLines.add(adviceOnBadFishTrade)
        advisor.specialLines.add(adviseToGetFish)
        advisor.specialLines.add(chideForBadDeal)
        game.applyCultureToCharacter(tutorialCulture.name, advisor)

        game.addPlayer(advisor)

        val dad = GameCharacter("Mayren", "assets/portraits/King.png", true, pcCapital.location, game)
        dad.resources.set(PlayerResourceTypes.GOLD_NAME, 100)
        dad.specialScoreGenerators.add(MayronScoreGen())
        dad.specialLines.add(talkToDadTrigger4)

        game.applyTitleToCharacter(capitals.capitalOf(territoryLogic.territories().first { it.name == "Worthford" }).generateCountTitle(), dad)
        game.applyTitleToCharacter(pcCapital.generateCountTitle(), dad)
        game.applyTitleToCharacter(danswada.generateKingTitle(), dad)
        game.addPlayer(dad)

        val names = Stack<String>()
        names.addAll(listOf("Faceperson", "De Puce", "Countington", "Fred", "Fredmark", "Billybob", "Tim", "Starwin", "Artyom", "Elsvin", "Krolm", "Ashta"))
        capitals.capitals.forEach {
            if(capitals.countOfCaptial(it.terId) == null){
                val NPC =
                    GameCharacter("Lord " + names.pop(), "assets/portraits/faceman.png", true, it.location, game)
                game.addPlayer(NPC)
                game.applyTitleToCharacter(it.generateCountTitle(), NPC)
            }
        }

        game.endTurn()
        return game
    }

}
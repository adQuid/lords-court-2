package scenario.tutorial

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
import game.culture.Topic
import gamelogic.government.Minister

object TutorialGameSetup {

    val TUTORIAL_GAME_MAP = "maps/testland"
    val TUTORIAL_PLAYER_NAME = "Essrader"

    fun setupAgricultureGame(): Game {
        val territories = TerritoryMap.fromMap(TUTORIAL_GAME_MAP)
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

        val tutorialCulture = Culture("Kairethtutorial", mutableSetOf(
            Topic(
                "How to Govern",
                "Writ",
                "I'm sure that, growing up, you mostly heard about what the kingdom was up to from your father directly, but for most of us the news came from the enactment of a writ. That's where the orders of the ruler are announced in the throne room for all to hear, and displayed for the land's agents to interpret. In a civilized land like ours, even simple transfers to and from the treasury require documentation. We've already set up a workroom for you here, which I suggest you get familiar with."
            ),
            Topic("Economics", "Capital Stocks", "\"Capital Stocks\" is a term used to describe anything we have here in our storeroom...I mean treasury. Any writ you make will be backed by these resources."),
            Topic("Economics", "Charity", "An important duty of any lord is to provide for the common folk in times of need. Whatever food we have in our capital stocks will be distributed out to those who don't have enough to feed themselves."),
            Topic("Economics", "Taxes", "The portion of each harvest given over to us. Did your father never teach you what taxes are?")
        ))
        game.cultures.add(tutorialCulture)

        val pcCapital = capitals.capitals.first()
        pcCapital.resources.set(PlayerResourceTypes.GOLD_NAME, 100)

        val PC = GameCharacter(TUTORIAL_PLAYER_NAME, "assets/general/conversation frame.png", false, pcCapital.location, game)
        PC.specialLines.add(talkToDadTrigger1)
        PC.specialLines.add(talkToDadTrigger2)
        PC.specialLines.add(talkToDadTrigger3)
        game.addPlayer(PC)

        val advisor = GameCharacter("Kaireth", "assets/portraits/Kaireth.png", true, pcCapital.location, game)
        game.applyTitleToCharacter(Minister(pcCapital), advisor)
        advisor.specialLines.add(adviseToTalkToDad)
        advisor.specialLines.add(adviceOnBadFishTrade)
        advisor.specialLines.add(adviseToGetFish)
        advisor.specialLines.add(chideForBadDeal)

        game.addPlayer(advisor)

        val dad = GameCharacter("Mayren", "assets/portraits/King.png", true, pcCapital.location, game)
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
                //game.addPlayer(NPC)
                //game.applyTitleToCharacter(it.generateCountTitle(), NPC)
            }
        }

        game.endTurn()
        return game
    }

}
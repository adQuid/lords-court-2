package scenario.tutorial

import game.Game
import game.culture.Culture
import gamelogic.government.Capital
import gamelogic.government.GovernmentLogicModule
import gamelogic.government.Kingdom
import gamelogic.playerresources.PlayerResourceModule
import gamelogic.territory.TerritoryLogicModule
import gamelogic.territory.TerritoryMap
import game.GameCharacter
import java.util.*
import game.culture.Topic
import gamelogic.economics.EconomicsLogicModule
import gamelogic.government.Minister
import gamelogic.government.laws.Charity
import gamelogic.resources.ResourceTypes
import gamelogic.territory.mapobjects.Structure
import gamelogic.territory.mapobjects.StructureType

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

        val economics = EconomicsLogicModule()

        val game = Game(listOf(PlayerResourceModule(), territoryLogic, capitals, economics), "tutorial")

        val tutorialCulture = Culture("Kairethtutorial", mutableSetOf(
            Topic(
                "How to Govern",
                "Writ",
                "I'm sure that, growing up, you mostly heard about what the kingdom was up to from your father directly, but for most of us the news came from the enactment of a writ. That's where the orders of the ruler are announced in the throne room for all to hear, and displayed for the land's agents to interpret. In a civilized land like ours, even simple transfers to and from the treasury require documentation. We've already set up a workroom for you here, which I suggest you get familiar with."
            ),
            Topic("Economics", "Crops", "The commoners only get one harvest per year, but if God wishes it that harvest will last the whole year. The fields don't wait, so it is absolutely essential that the people are allowed time to be at their farms for both planting and harvest season. During winter, it is essential we have enough food to last, for God will not provide any more until the frosts melt."),
            Topic("Economics", "Capital Stocks", "\"Capital Stocks\" is a term used to describe anything we have here in our storeroom...I mean treasury. Any writ you make will be backed by these resources."),
            Topic("Economics", "Charity", "An important duty of any lord is to provide for the common folk in times of need. Whatever food we have in our capital stocks will be distributed out to those who don't have enough to feed themselves."),
            Topic("Economics", "Taxes", "In more savage lands, rulers may be content to simply demand tributes whenever they feel the urge, in our kingdom there are orderly rules for the collection of taxes. The most important of these is the harvest, where we send our men to take a portion of every farm's yield for the common good.")
        ))
        game.cultures.add(tutorialCulture)

        val pcCapital = capitals.capitals.first()
        pcCapital.resources.set(ResourceTypes.GOLD_NAME, 100)
        pcCapital.enactLaw(Charity(true))

        val PC = GameCharacter(TUTORIAL_PLAYER_NAME, "assets/general/conversation frame.png", false, pcCapital.location, game)
        PC.specialLines.add(talkToDadTrigger1())
        PC.specialLines.add(talkToDadTrigger2())
        PC.specialLines.add(talkToDadTrigger3())
        game.addPlayer(PC)

        val advisor = GameCharacter("Kaireth", "assets/portraits/Kaireth.png", true, pcCapital.location, game)
        game.applyTitleToCharacter(Minister(pcCapital), advisor)
        advisor.specialLines.add(adviseToTalkToDad())
        advisor.specialLines.add(adviceOnBadFishTrade())
        advisor.specialLines.add(adviseToGetFish())
        advisor.specialLines.add(chideForBadDeal())

        game.addPlayer(advisor)

        val dad = GameCharacter("Mayren", "assets/portraits/King.png", true, pcCapital.location, game)
        dad.specialScoreGenerators.add(MayronScoreGen())
        dad.specialLines.add(talkToDadTrigger4())

        game.applyTitleToCharacter(capitals.capitalOf(territoryLogic.territories().first { it.name == "Worthford" }).generateCountTitle(), dad)
        game.applyTitleToCharacter(pcCapital.generateCountTitle(), dad)
        game.applyTitleToCharacter(danswada.generateKingTitle(), dad)
        game.addPlayer(dad)

        pcCapital.territory!!.structures.add(Structure(PC.id, StructureType.typeByName("Water Mill")))
        pcCapital.territory!!.structures.add(Structure(PC.id, StructureType.typeByName("Water Mill")))

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
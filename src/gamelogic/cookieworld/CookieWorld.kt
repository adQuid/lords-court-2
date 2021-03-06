package gamelogic.cookieworld

import aibrain.Plan
import aibrain.Score
import game.*
import game.titlemaker.CookieWorldTitleFactory
import game.titles.Baker
import game.titles.Milkman
import gamelogic.cookieworld.actionTypes.BakeCookies
import gamelogic.cookieworld.actionTypes.GetMilk
import gamelogic.cookieworld.actionTypes.WasteTime
import javafx.scene.control.Button
import shortstate.ShortStateCharacter
import shortstate.report.DeliciousnessReportFactory

class CookieWorld: GameLogicModule {

    companion object{
        val type = "Cookieworld"

        fun getCookieWorld(game: Game): CookieWorld {
            return game.moduleOfType("Cookieworld") as CookieWorld
        }
    }

    override val type = Companion.type

    val DELICIOUSNESS_NAME = "deliciousness"
    var deliciousness = 0.0
    val HAS_MILK_NAME = "hasMilk"
    var hasMilk = mutableListOf<GameCharacter>()
    var locations = mutableListOf(Location(0,0))

    constructor() : super(listOf(DeliciousnessReportFactory()), CookieWorldTitleFactory, listOf()) {
        deliciousness = 0.0
        hasMilk = mutableListOf<GameCharacter>()
    }

    constructor(other: CookieWorld): super(listOf(DeliciousnessReportFactory()), CookieWorldTitleFactory, listOf()){
        deliciousness = other.deliciousness
        hasMilk = other.hasMilk.toMutableList()
        locations = other.locations.map { Location(it) }.toMutableList()
    }

    constructor(saveString: Map<String, Any>, game: Game): super(listOf(DeliciousnessReportFactory()), CookieWorldTitleFactory, listOf()){
        deliciousness = saveString[DELICIOUSNESS_NAME] as Double
        hasMilk = (saveString[HAS_MILK_NAME] as List<Int>).map { id -> game.characterById(id)}.toMutableList()
        locations = (saveString["locations"] as List<Map<String,Any>>).map { map -> Location(map) }.toMutableList()
    }

    override fun finishConstruction(game: Game) {

    }

    override fun locations(): Collection<Location> {
        return locations
    }




    override fun endTurn(game: Game){
        //do nothing
    }

    override fun specialSaveString(): Map<String, Any> {
        return mapOf(
            DELICIOUSNESS_NAME to deliciousness,
            HAS_MILK_NAME to hasMilk.map { it.id },
            "locations" to locations.map { it.saveString() }
        )
    }

    override fun planOptions(perspective: GameCharacter, importantPlayers: Collection<GameCharacter>): Collection<Plan> {
        val retval = mutableListOf<Plan>()

        importantPlayers.forEach {character ->
            retval.add(Plan(character, listOf(WasteTime()), 0.5))
            perspective.titles.forEach {
                if(it is Baker){
                    retval.add(Plan(character, listOf(BakeCookies()), 0.5))
                }
                if(it is Milkman){
                    retval.add(Plan(character, listOf(GetMilk(character)), 0.5))
                }
            }
        }

        return retval
    }

    override fun score(perspective: GameCharacter): Score {
        var retval = Score()
        if(hasMilk.contains(perspective)){
            retval.add("Deliciousness", {value -> "I will eat yummy cookies"},deliciousness * 1.0)
        }
        retval.add("Dummy", {value -> "Dummy score of ${value}"}, perspective.dummyScore)
        return retval
    }

}
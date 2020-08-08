package gamelogic.government

import game.Game
import game.GameCharacter
import game.Title
import game.action.Action
import game.titlemaker.CookieWorldTitleFactory
import shortstate.report.ReportFactory

class King: Title{
    override val importance = 100
    override val name: String
    val KINGDOM_NAME = "Kingdom"
    val kingdom: Kingdom
    override val reportsEntitled: List<ReportFactory>

    constructor(kingdom: Kingdom){
        this.kingdom = kingdom
        this.name = "King of of ${kingdom.name}"
        reportsEntitled = reportsEntitled()
    }

    constructor(saveString: Map<String, Any>, game: Game){
        val capitalLogic = game.moduleOfType(GovernmentLogicModule.type) as GovernmentLogicModule
        this.name = saveString[NAME_NAME] as String
        this.kingdom = capitalLogic.kingdomByName(saveString[KINGDOM_NAME] as String)
        reportsEntitled = reportsEntitled()
    }

    private fun reportsEntitled(): List<ReportFactory>{
        return listOf(

        )
    }

    override fun clone(): King {
        return King(kingdom)
    }

    override fun saveString(): Map<String, Any> {
        return hashMapOf(
            CookieWorldTitleFactory.TYPE_NAME to "King",
            NAME_NAME to name,
            REPORTS_NAME to reportsEntitled.map { report -> report.toString() },
            KINGDOM_NAME to kingdom.name
        )
    }

    override fun actionsReguarding(players: List<GameCharacter>): List<Action> {
        return listOf()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as King

        if (name != other.name) return false
        if (reportsEntitled != other.reportsEntitled) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + reportsEntitled.hashCode()
        return result
    }
}
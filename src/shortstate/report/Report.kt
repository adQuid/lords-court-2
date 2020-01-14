package shortstate.report

import game.Game

interface Report {

    fun apply(game: Game)

    fun saveString(): Map<String, Any>

    fun type(): ReportType
}
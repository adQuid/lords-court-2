package shortstate.report

import game.Game

interface Report {

    fun apply(game: Game)

    fun type(): ReportType
}
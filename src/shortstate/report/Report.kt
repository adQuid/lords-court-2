package shortstate.report

import game.Game

abstract class Report {

    abstract val type: String

    abstract fun apply(game: Game)

    abstract fun specialSaveString(): Map<String, Any>

    fun saveString(): Map<String, Any>{
        val retval = specialSaveString().toMutableMap()

        retval.putAll(
            mapOf<String, Any>(
                GlobalReportTypeFactory.TYPE_NAME to type
            )
        )

        return retval

    }

}
package shortstate.report

import game.Game
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import ui.Displayable
import ui.commoncomponents.PrettyPrintable
import ui.componentfactory.UtilityComponentFactory

abstract class Report: PrettyPrintable, Displayable {

    companion object{
        fun reportPane(report: Report, perspective: ShortStateCharacter): GridPane {
            val root = GridPane()

            root.add(UtilityComponentFactory.shortWideLabel(report.prettyPrint(UIGlobals.activeShortGame(), perspective)), 0, 0)
            root.add(UtilityComponentFactory.proportionalLabel("    "+report.detailedDescription(), 1.0, 0.8), 0,1)
            root.add(UtilityComponentFactory.backButton(),0,2)

            return root
        }
    }

    abstract val type: String
    val turn: Int

    constructor(){
        turn = -1
    }

    constructor(game: Game){
        turn = game.turn
    }

    constructor(saveString: Map<String, Any>){
        turn = saveString["turn"] as Int
    }

    abstract fun apply(game: Game)

    abstract fun specialSaveString(): Map<String, Any>

    abstract fun detailedDescription(): String

    override fun universalDisplay(perspective: ShortStateCharacter?): Scene{
        return Scene(reportPane(this, perspective!!))
    }

    fun saveString(): Map<String, Any>{
        val retval = specialSaveString().toMutableMap()

        retval.putAll(
            mapOf(
                GlobalReportTypeFactory.TYPE_NAME to type,
                "turn" to turn
            )
        )

        return retval

    }

}
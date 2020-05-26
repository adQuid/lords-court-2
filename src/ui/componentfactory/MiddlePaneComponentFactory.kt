package ui.componentfactory

import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.text.Font
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.specialdisplayables.OptionsMenu
import ui.specialdisplayables.selectionmodal.SelectionModal

object MiddlePaneComponentFactory {

    fun middlePane(perspective: ShortStateCharacter): Pane {
        val retval = GridPane()

        val turnDisplay = Label(UIGlobals.activeGame().turnName())
        turnDisplay.font = Font(20.0)
        turnDisplay.setMinSize(UIGlobals.totalWidth()/6, UIGlobals.totalHeight() / 10)
        retval.add(turnDisplay, 0,0)
        val statsDisplay = Label("Energy: " + perspective.energy + "/1000")
        statsDisplay.setMinSize(UIGlobals.totalWidth()/6, UIGlobals.totalHeight() / 10)
        retval.add(statsDisplay, 1,0)


        retval.add(UtilityComponentFactory.iconButton("assets/general/reportsIcon.png", "View Reports from this Turn", {
            UIGlobals.focusOn(
                SelectionModal( "Reports",
                    UtilityComponentFactory.reports(perspective),
                    { report -> println(report) })
            )
        }), 2,0)

        retval.add(UtilityComponentFactory.iconButton("assets/general/writsIcon.png", "View Writs in your Possession", {
            UIGlobals.focusOn(
                SelectionModal( "Writs",
                    UtilityComponentFactory.writs(perspective),
                    { writ -> UIGlobals.focusOn(writ) })
            )
        }), 3,0)

        val optionsButton = UtilityComponentFactory.shortButton("Options", EventHandler { UIGlobals.focusOn(OptionsMenu()) })
        retval.add(optionsButton, 4,0)

        return retval
    }

}
package ui.componentfactory

import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.text.Font
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.BOTTOM_BAR_PORTION
import ui.specialdisplayables.EndTurnMenu
import ui.specialdisplayables.NewSceneSelector
import ui.specialdisplayables.OptionsMenu
import ui.specialdisplayables.selectionmodal.SelectionModal

object MiddlePaneComponentFactory {

    fun middlePane(perspective: ShortStateCharacter, locked: Boolean): Pane {
        val retval = GridPane()

        val turnDisplay = Label(UIGlobals.activeGame().turnName())
        turnDisplay.font = Font(20.0)
        turnDisplay.setMinSize(UIGlobals.totalWidth()/6, UIGlobals.totalHeight() * BOTTOM_BAR_PORTION)
        retval.add(turnDisplay, 0,0)
        val statsDisplay = Label("Energy: " + perspective.energy + "/1000")
        statsDisplay.setMinSize(UIGlobals.totalWidth()/6, UIGlobals.totalHeight() * BOTTOM_BAR_PORTION)
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

        val optionsButton = UtilityComponentFactory.iconButton("assets/general/optionsIcon.png", "Options", { UIGlobals.focusOn(OptionsMenu()) })
        retval.add(optionsButton, 4,0)

        if(!locked){
            retval.add(UtilityComponentFactory.iconButton("assets/general/newSceneIcon.png", "Go somewhere else", { UIGlobals.focusOn(
                NewSceneSelector.newSceneSelector(perspective)
            )
            }), 5,0)

            retval.add(UtilityComponentFactory.iconButton("assets/general/endTurnIcon.png", "End turn", { UIGlobals.focusOn(
                EndTurnMenu()
            )}), 6,0)
        }

        return retval
    }

}
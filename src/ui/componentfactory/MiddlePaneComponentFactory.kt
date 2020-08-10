package ui.componentfactory

import javafx.event.EventHandler
import javafx.scene.Node
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
import ui.specialdisplayables.PersonalResourcesView
import ui.specialdisplayables.selectionmodal.SelectionModal

object MiddlePaneComponentFactory {

    fun middlePane(perspective: ShortStateCharacter, locked: Boolean): Pane {
        val retval = GridPane()

        val turnDisplay = Label(UIGlobals.activeGame().turnName())
        turnDisplay.font = Font(20.0)
        turnDisplay.setMinSize(UIGlobals.totalWidth()/6, UIGlobals.totalHeight() * BOTTOM_BAR_PORTION)
        retval.add(turnDisplay, 0,0)
        val statsDisplay = Label("Energy: " + perspective.energy + "/1000")
        UtilityComponentFactory.applyTooltip(statsDisplay, "Every action will cost energy. Running out of energy with leave you with no option but to end turn.")
        statsDisplay.setMinSize(UIGlobals.totalWidth()/6, UIGlobals.totalHeight() * BOTTOM_BAR_PORTION)
        retval.add(statsDisplay, 1,0)

        nonModifyingComponents(perspective).entries.forEach { retval.add(it.value, it.key, 0) }

        if(!locked){
            modifyingComponents(perspective, locked).entries.forEach { retval.add(it.value, it.key, 0) }
        } else {
            modifyingComponents(perspective, locked).entries.forEach { retval.add(it.value, it.key, 0); retval.children[it.key].isDisable = true }
        }

        return retval
    }

    private fun nonModifyingComponents(perspective: ShortStateCharacter): Map<Int, Node>{
        return mapOf(
            2 to UtilityComponentFactory.iconButton("assets/general/reportsIcon.png", "View Reports from this Turn", {
                UIGlobals.focusOn(
                    SelectionModal( "Reports",
                        UtilityComponentFactory.reports(perspective),
                        { report -> println(report) })
                )
            }),
            3 to UtilityComponentFactory.iconButton("assets/general/writsIcon.png", "View Writs in your Possession", {
                UIGlobals.focusOn(
                    SelectionModal( "Writs",
                        UtilityComponentFactory.writs(perspective),
                        { writ -> UIGlobals.focusOn(writ) })
                )
            }),
            4 to UtilityComponentFactory.iconButton("assets/general/personalResourcesIcon.png", "View Resources in your Possession", {
                UIGlobals.focusOn(PersonalResourcesView())
            }),
            5 to UtilityComponentFactory.iconButton("assets/general/optionsIcon.png", "Options", { UIGlobals.focusOn(OptionsMenu()) })
        )
    }

    private fun modifyingComponents(perspective: ShortStateCharacter, disabled: Boolean): Map<Int, Node>{
        val extra = if(disabled) "Disabled" else ""
        return mapOf(
            6 to UtilityComponentFactory.iconButton("assets/general/newSceneIcon${extra}.png", "Go somewhere else", { UIGlobals.focusOn(
                NewSceneSelector.newSceneSelector(perspective)
            )
            }),
            7 to UtilityComponentFactory.iconButton("assets/general/endTurnIcon${extra}.png", "End turn", { UIGlobals.focusOn(
                EndTurnMenu()
            )})
        )
    }
}
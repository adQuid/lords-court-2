package ui.commoncomponents

import game.action.Action
import game.action.ActionComponentFactory
import javafx.event.EventHandler
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import main.UIGlobals
import ui.PerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory

class AppendableActionList {

    fun actionList(collection: MutableSet<Action>, addSelector: PerspectiveDisplayable?): Pane {
        val retval = GridPane()
        retval.style = "-fx-background-color: tan"

        var behavior: (Action) -> Unit
        if(addSelector != null){
            behavior = {action -> UIGlobals.focusOn(ActionComponentFactory(action, collection))}

            val newActionButton = UtilityComponentFactory.shortWideButton("Add Action",
                EventHandler {_ -> UIGlobals.focusOn(addSelector)})
            retval.add(newActionButton, 0 , 1)
        }else{
            behavior = {}
        }


        retval.add(UtilityComponentFactory.basicList(collection.toList(), behavior, UIGlobals.totalWidth(), UIGlobals.totalHeight()), 0, 0)

        return retval
    }
}
package ui.commoncomponents

import game.action.Action
import javafx.event.EventHandler
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import main.UIGlobals
import ui.PerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory

class AppendableList<T> {

    fun actionList(collection: Collection<Action>, addSelector: PerspectiveDisplayable?): Pane {
        val retval = GridPane()

        var behavior: (Action) -> Unit
        /*if(addSelector != null){
            behavior = {action -> (collection as MutableCollection).remove(action);
                UIGlobals.refresh()}

            val newActionButton = UtilityComponentFactory.shortWideButton("Add Action",
                EventHandler {_ -> UIGlobals.focusOn(addSelector)})
            retval.add(newActionButton, 0 , 1)
        }else{
            behavior = {}
        }*/
        behavior = {action -> UIGlobals.focusOn(action)}

        retval.add(UtilityComponentFactory.basicList(collection.toList(), behavior, UIGlobals.totalWidth(), UIGlobals.totalHeight() * (5.0/6.0)), 0, 0)

        retval.setPrefSize(UIGlobals.totalWidth(), (5* UIGlobals.totalHeight())/6)
        return retval
    }
}
package ui.commoncomponents

import javafx.event.EventHandler
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import main.Controller
import main.UIGlobals
import ui.Displayable
import ui.componentfactory.UtilityComponentFactory
import ui.totalHeight
import ui.totalWidth

class AppendableList<T> {

    fun actionList(collection: Collection<T>, addSelector: Displayable?): Pane {
        val retval = GridPane()

        var behavior: (T) -> Unit
        if(addSelector != null){
            behavior = {action -> (collection as MutableCollection).remove(action);
                UIGlobals.refresh()}

            val newActionButton = UtilityComponentFactory.shortWideButton("Add Action",
                EventHandler {_ -> UIGlobals.focusOn(addSelector)})
            retval.add(newActionButton, 0 , 1)
        }else{
            behavior = {}
        }

        retval.add(UtilityComponentFactory.basicList(collection.toList(), behavior, totalWidth, totalHeight * (5.0/6.0)), 0, 0)

        retval.setPrefSize(totalWidth, (5* totalHeight)/6)
        return retval
    }
}
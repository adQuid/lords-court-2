package ui.specialdisplayables.selectionmodal

import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.GridPane
import javafx.scene.control.ListCell
import javafx.scene.layout.Pane
import javafx.scene.text.Font
import javafx.scene.text.Text
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.Displayable
import ui.commoncomponents.PrettyPrintable
import ui.componentfactory.UtilityComponentFactory


class SelectionModal<T>: Displayable {

    val parent = UIGlobals.GUI()
    val options: List<Tab<T>>
    val closeAction: (T) -> Unit

    var focusedTab: Tab<T>

    constructor(options: List<Tab<T>>, action: (T) -> Unit){
        this.options = options
        this.focusedTab = options[0]
        this.closeAction = action
    }

    override fun display(perspective: ShortStateCharacter): Scene {
        return getScene()
    }

    fun getScene(): Scene {
        val pane = GridPane()

        val data = FXCollections.observableArrayList<T>()
        data.addAll(focusedTab.items)
        val listView = ListView<T>(data)
        listView.items = data
        listView.setPrefSize(parent.totalWidth,parent.totalHeight * (5.0/6.0))
        listView.setCellFactory({ _: ListView<T> ->
            ActionPickCell(
                closeAction
            )
        })

        val btn8 = UtilityComponentFactory.shortWideButton("Cancel", EventHandler { parent.defocus()})
        pane.add(topPane(),0,0)
        pane.add(listView,0,1)
        pane.add(btn8, 0, 2)

        return Scene(pane, parent.totalWidth, parent.totalHeight)
    }

    private fun topPane(): Pane {
        val topPane = GridPane()
        var index = 0
        options.forEach {
            val topic = Button(it.title)
            if(it == focusedTab){
                topic.font = Font(28.0)
            } else {
                topic.onAction = EventHandler {_ -> focusedTab = it; parent.display()}
            }
            topic.setMinSize(parent.totalWidth / options.size, parent.totalHeight / 12)
            topPane.add(topic, index++, 0)
        }
        return topPane
    }

    internal class ActionPickCell<T>(val closeAction: (T) -> Unit) : ListCell<T>() {
        public override fun updateItem(item: T?, empty: Boolean) {
            if(item != null){
                super.updateItem(item, empty)
                val displayText = if(item is PrettyPrintable){ item.prettyPrint(UIGlobals.GUI().shortGame(), UIGlobals.playingAs()) } else{ item.toString() }
                val text = Text(displayText)
                text.font = Font(20.0)
                this.graphic = text
                this.onMouseClicked = EventHandler{_ -> closeAction(item)}
            }
        }
    }
}
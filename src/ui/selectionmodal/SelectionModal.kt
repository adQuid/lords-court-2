package ui.selectionmodal

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
import ui.MainUI
import ui.componentfactory.UtilityComponentFactory


class SelectionModal<T> {

    val parent: MainUI
    val options: List<Tab<T>>
    val closeAction: (T) -> Unit

    var focusedTab: Tab<T>

    constructor(parent: MainUI, options: List<Tab<T>>, action: (T) -> Unit){
        this.parent = parent
        this.options = options
        this.focusedTab = options[0]
        this.closeAction = action
    }

    fun getScene(): Scene {
        val pane = GridPane()

        val data = FXCollections.observableArrayList<T>()
        data.addAll(focusedTab.items)
        val listView = ListView<T>(data)
        listView.items = data
        listView.setPrefSize(parent.totalWidth,parent.totalHeight * (5.0/6.0))
        listView.setCellFactory({ _: ListView<T> -> ActionPickCell(closeAction) })

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
                topic.font = Font(18.0)
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
                this.graphic = Text(item.toString())
                this.onMouseClicked = EventHandler{_ -> closeAction(item)}
            }
        }
    }
}
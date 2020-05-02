package ui.componentfactory

import game.Writ
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.text.Text
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.report.Report
import ui.specialdisplayables.NewSceneSelector
import ui.specialdisplayables.OptionsMenu
import ui.specialdisplayables.selectionmodal.SelectionModal
import ui.specialdisplayables.selectionmodal.Tab

object UtilityComponentFactory {

    fun imageView(url: String): ImageView {
        val path = url.replace("//","\\")
        val image = Image(path)
        val retval = ImageView(image)
        retval.fitHeight = (UIGlobals.totalHeight()) * (8.0 / 10.0)
        retval.fitWidth = UIGlobals.totalWidth()
        return retval
    }

    fun bottomPane(buttons: List<Button>, perspective: ShortStateCharacter): Pane{

        val buttonsPane = GridPane()
        for(index in 0..3){
            if(index < buttons.size){
                buttonsPane.add(buttons[index], (index % 4), if(index > 3) 1 else 0)
            } else {
                buttonsPane.add(shortButton("", null), (index % 4), if(index > 3) 1 else 0)
            }
        }

        val retval = GridPane()
        retval.add(middlePane(perspective), 0, 0)
        retval.add(buttonsPane, 0,1)

        return retval
    }

    private fun middlePane(perspective: ShortStateCharacter): Pane{
        val retval = GridPane()

        val statsDisplay = Label("Energy: " + perspective.energy + "/1000")
        statsDisplay.setMinSize(UIGlobals.totalWidth()/6, UIGlobals.totalHeight() / 10)
        retval.add(statsDisplay, 0,0)

        val reportsDisplay = ImageView(Image("assets/general/reportsIcon.png"))
        reportsDisplay.fitHeight = UIGlobals.totalHeight()/10
        reportsDisplay.fitWidth = UIGlobals.totalWidth()/12
        reportsDisplay.onMouseClicked =  EventHandler { _ -> UIGlobals.focusOn(
            SelectionModal(
                reports(perspective),
                { report -> println(report) })
        )
        }
        retval.add(reportsDisplay, 1,0)

        val writsDisplay = ImageView(Image("assets/general/writsIcon.png"))
        writsDisplay.fitHeight = UIGlobals.totalHeight()/10
        writsDisplay.fitWidth = UIGlobals.totalWidth()/12
        writsDisplay.onMouseClicked =  EventHandler { _ -> UIGlobals.focusOn(
            SelectionModal(
                writs(perspective),
                { report -> println(report) })
        )
        }
        retval.add(writsDisplay, 2,0)

        val optionsButton = UtilityComponentFactory.shortButton("Options", EventHandler { UIGlobals.focusOn(OptionsMenu()) })
        retval.add(optionsButton, 3,0)

        return retval
    }

    fun newSceneButton(perspective: ShortStateCharacter): Button {
        return shortButton("Go Somewhere Else",
                EventHandler { _ -> UIGlobals.focusOn(
                    NewSceneSelector.newSceneSelector(perspective)
                )
            }
        )
    }



    fun backButton(): Button{
        return proportionalBackButton(1.0)
    }

    fun proportionalBackButton(proportion: Double): Button{
        return proportionalButton("Back", EventHandler { UIGlobals.defocus() }, proportion)
    }

    fun <T> basicList(items: List<T>, onClick: (T) -> Unit, width: Double, height: Double): ListView<T> {
        val data = FXCollections.observableArrayList<T>()
        data.addAll(items)
        val listView = ListView<T>(data)
        listView.items = data
        listView.setPrefSize(width,height)
        listView.setCellFactory({ _: ListView<T> -> ActionPickCell(onClick) })

        return listView
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

    fun shortButton(text: String, action: EventHandler<MouseEvent>?): Button {
        return shortButton(text, action, 1)
    }

    fun shortButton(text: String, action: EventHandler<MouseEvent>?, specialWidth: Int): Button {
        return shortButton(text, action, specialWidth.toDouble())
    }

    fun shortButton(text: String, action: EventHandler<MouseEvent>?, specialWidth: Double): Button {
        val retval = proportionalButton(text, action, 4.0)
        retval.setMinSize(retval.minWidth*specialWidth, retval.minHeight)
        return retval
    }

    fun shortWideButton(text: String, action: EventHandler<MouseEvent>?): Button {
        return proportionalButton(text, action, 1.0)
    }

    fun shortWideTextField(text: String): TextField{
        val retval = TextField(text)
        setSize(retval, 1.0)
        return retval
    }

    fun shortWideLabel(text: String): Label{
        val retval = Label(text)
        setSize(retval, 1.0)
        return retval
    }

    fun proportionalButton(text: String, action: EventHandler<MouseEvent>?, proportion: Double): Button{
        val retval = Button(text)
        setSize(retval, proportion)
        if (action != null) {
            retval.onMouseClicked = action
        }
        return retval
    }

    private fun setSize(node: Control, proportion: Double){
        node.setMinSize(UIGlobals.totalWidth() / proportion, UIGlobals.totalHeight() / 10)
    }

    fun reports(perspective: ShortStateCharacter): List<Tab<Report>>{
        val reportOptions = perspective.knownReports
        val reportTab = Tab<Report>("Reports", reportOptions)

        return listOf(reportTab)
    }

    fun writs(perspective: ShortStateCharacter): List<Tab<Writ>>{
        val complete = perspective.player.writs.filter { it.complete() }
        val completeTab = Tab<Writ>("Complete", complete)
        val incomplete = perspective.player.writs.filter { !it.complete() }
        val incompleteTab = Tab<Writ>("Incomplete", incomplete)

        return listOf(completeTab, incompleteTab)
    }
}
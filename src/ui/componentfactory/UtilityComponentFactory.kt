package ui.componentfactory

import game.Writ
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.image.PixelFormat
import javafx.scene.image.WritableImage
import javafx.scene.input.MouseEvent
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.util.Duration
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.report.Report
import ui.BOTTOM_BAR_PORTION
import ui.specialdisplayables.NewSceneSelector
import ui.specialdisplayables.selectionmodal.Tab

object UtilityComponentFactory {

    fun imageView(url: String, height: Double): ImageView {
        val retval = ImageView(imageFromPath(url))
        retval.fitHeight = (UIGlobals.totalHeight()) * height
        retval.fitWidth = UIGlobals.totalWidth()
        return retval
    }

    fun imageFromPath(url: String): Image{
        val path = url.replace("//","\\")

        var image: Image
        try{
            image = Image(path)
        }catch (e: IllegalArgumentException){
            try{
                image = Image(path+".png")
            }catch (e: IllegalArgumentException){
                image = Image(path+".jpeg")
            }
        }
        return image
    }

    fun writableImageView(height: Double): ImageView {
        val image = WritableImage(2000, 1500)
        val retval = ImageView(image)
        retval.fitHeight = (UIGlobals.totalHeight()) * height
        retval.fitWidth = UIGlobals.totalWidth()
        return retval
    }

    fun refreshImageView(view: ImageView): ImageView {
        view.image = WritableImage(2000, 1500)
        return view
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
        return shortProportionalTextField(text, 1.0)
    }

    fun shortProportionalTextField(text: String, proportion: Double): TextField{
        val retval = TextField(text)
        setSize(retval, proportion)
        return retval
    }

    fun extraShortWideLabel(text: String): Label {
        val retval = Label(text)
        setSize(retval, 1.0)
        retval.minHeight = UIGlobals.totalHeight()/20
        retval.alignment = Pos.CENTER
        return retval
    }

    fun shortWideLabel(text: String): Label{
        return shortProportionalLabel(text, 1.0)
    }

    fun shortProportionalLabel(text: String, proportion: Double): Label{
        val retval = Label(text)
        setSize(retval, proportion)
        retval.alignment = Pos.CENTER
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
        val reportTab = Tab("Reports", reportOptions)

        return listOf(reportTab)
    }

    fun writs(perspective: ShortStateCharacter): List<Tab<Writ>>{
        val complete = perspective.player.writs.filter { it.complete() }
        val completeTab = Tab("Complete", complete)
        val incomplete = perspective.player.writs.filter { !it.complete() }
        val incompleteTab = Tab("Incomplete", incomplete)

        return listOf(completeTab, incompleteTab)
    }

    fun applyTooltip(node: Node, text: String?): Node{
        if(text != null){
            val tooltip = Tooltip(text)
            tooltip.font = Font(12.0)
            tooltip.showDelay = Duration(100.0)
            Tooltip.install(node, tooltip)
        }

        return node
    }

    fun iconButton(image: String, tooltip: String, action: () -> Unit): ImageView{
        val retval = ImageView(Image(image))
        retval.fitHeight = UIGlobals.totalHeight() * BOTTOM_BAR_PORTION
        retval.fitWidth = UIGlobals.totalWidth()/12
        applyTooltip(retval, tooltip)
        retval.onMouseClicked =  EventHandler { _ -> action()}
        return retval
    }
}
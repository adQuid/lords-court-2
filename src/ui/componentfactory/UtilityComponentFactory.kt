package ui.componentfactory

import game.Writ
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.image.PixelFormat
import javafx.scene.image.WritableImage
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.util.Duration
import jdk.jshell.execution.Util
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.report.Report
import ui.BOTTOM_BAR_PORTION
import ui.Describable
import ui.specialdisplayables.NewSceneSelector
import ui.specialdisplayables.selectionmodal.Tab

object UtilityComponentFactory {

    fun imageView(url: String, height: Double): ImageView {
        return imageView(url, height, 1.0)
    }

    fun imageView(url: String, height: Double, width: Double): ImageView {
        val retval = ImageView(imageFromPath(url))
        retval.fitHeight = (UIGlobals.totalHeight()) * height
        retval.fitWidth = UIGlobals.totalWidth() / width
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
                try{
                    image = Image(path+".jpeg")
                } catch (e: Exception){
                    throw Exception("Image file $path not found!")
                }
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

    fun newSceneButton(perspective: ShortStateCharacter): Node {
        return shortButton("Go Somewhere Else",
                EventHandler { _ -> UIGlobals.focusOn(
                    NewSceneSelector.newSceneSelector(perspective)
                )
            }
        )
    }

    fun backButton(): Node{
        return proportionalBackButton(1.0)
    }

    fun proportionalBackButton(proportion: Double): Node{
        return proportionalButton("Back", EventHandler { UIGlobals.defocus() }, proportion)
    }

    fun <T: Describable> basicList(items: List<T>, onClick: (T) -> Unit, width: Double, height: Double): ListView<T> {
        val data = FXCollections.observableArrayList<T>()
        data.addAll(items)
        val listView = ListView<T>(data)
        listView.items = data
        listView.setPrefSize(width,height)
        listView.setCellFactory({ _: ListView<T> -> ActionPickCell(onClick) })

        return listView
    }

    internal class ActionPickCell<T: Describable>(val closeAction: (T) -> Unit) : ListCell<T>() {
        public override fun updateItem(item: T?, empty: Boolean) {
            if(item != null){
                super.updateItem(item, empty)
                this.graphic = Text(item.description())
                this.onMouseClicked = EventHandler{_ -> closeAction(item)}
            }
        }
    }

    fun shortButton(text: String, action: EventHandler<MouseEvent>?): Node {
        return shortButton(text, action, 1)
    }

    fun shortButton(text: String, action: EventHandler<MouseEvent>?, specialWidth: Int): Node {
        return shortButton(text, action, specialWidth.toDouble())
    }

    fun shortButton(text: String, action: EventHandler<MouseEvent>?, specialWidth: Double): Node {
        val retval = proportionalButton(text, action, 4.0)
        return retval
    }

    fun shortWideButton(text: String, action: EventHandler<MouseEvent>?): Node {
        return proportionalButton(text, action, 1.0)
    }

    fun shortWideClickedButton(text: String, action: EventHandler<MouseEvent>?): Node {
        return proportionalButton(text, action, 1.0, 0.1, true)
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
        retval.padding = Insets(15.0)
        retval.isWrapText = true
        return retval
    }

    fun proportionalButton(text: String, action: EventHandler<MouseEvent>?, proportion: Double): Node{
        return proportionalButton(text, action, proportion, 0.1, false)
    }

    fun proportionalButton(text: String, action: EventHandler<MouseEvent>?, width: Double, height: Double, clicked: Boolean): Node{
        val image = if(clicked){ imageView("assets/general/generalButtonSelected.png", height, width)} else {imageView("assets/general/generalButton.png", height, width)}

        val text = Text(text)
        text.font = Font(20.0)

        val retval = StackPane()
        retval.children.add(image)
        retval.children.add(text)
        if (action != null) {
            retval.onMouseClicked = EventHandler { event -> retval.children.set(0, imageView("assets/general/generalButtonSelected.png", height, width)); image.fitWidth = UIGlobals.totalWidth() / width; action.handle(event) }
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
        val includingPlayer = UIGlobals.activeGame().players.filter{it != perspective.player}.flatMap { it.writs }.filter { it.signatories.contains(perspective.player) }
        val includingPlayerTab = Tab("Signed by You", includingPlayer)

        return listOf(completeTab, incompleteTab, includingPlayerTab)
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
        val retval = ImageView(imageFromPath(image))
        retval.fitHeight = UIGlobals.totalHeight() * BOTTOM_BAR_PORTION
        retval.fitWidth = UIGlobals.totalWidth()/12
        applyTooltip(retval, tooltip)
        retval.onMouseClicked =  EventHandler { _ -> action()}
        return retval
    }

}
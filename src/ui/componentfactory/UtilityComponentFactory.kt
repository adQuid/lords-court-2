package ui.componentfactory

import game.Writ
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Background
import javafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import javafx.util.Duration
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.report.Report
import ui.BOTTOM_BAR_PORTION
import ui.Describable
import ui.commoncomponents.PrettyPrintable
import ui.specialdisplayables.NewSceneSelector
import ui.specialdisplayables.selectionmodal.Tab
import javax.swing.GroupLayout

object UtilityComponentFactory {

    val paper_background = "url(assets/general/generalInfo.png)"

    fun imageView(url: String, height: Double): ImageView {
        return imageView(url, height, 1.0)
    }

    fun imageView(url: String, height: Double, inputwidth: Double): ImageView {
        //temporary workaround to handle the old way width was input
        var width = inputwidth
        if(inputwidth > 1.0){
            width = 1/inputwidth
        }

        val retval = ImageView(imageFromPath(url))
        retval.fitHeight = (UIGlobals.totalHeight()) * height
        retval.fitWidth = UIGlobals.totalWidth() * width
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
        listView.style = "-fx-background-image: $paper_background"
        listView.items = data
        listView.setPrefSize(width,height)
        listView.setCellFactory({ _: ListView<T> -> ActionPickCell(onClick) })

        return listView
    }

    internal class ActionPickCell<T: Describable>(val closeAction: ((T) -> Unit)?) : ListCell<T>() {
        public override fun updateItem(item: T?, empty: Boolean) {
            if(item != null){
                super.updateItem(item, empty)
                val displayText = item.description()

                if(closeAction != null){
                    this.graphic = shortWideButton(displayText, EventHandler { })
                    this.onMouseClicked = EventHandler { _ -> this.graphic = UtilityComponentFactory.shortWideClickedButton(displayText, EventHandler { });  Platform.runLater { Thread.sleep(100); closeAction!!(item) }}
                    this.padding = Insets.EMPTY
                } else {
                    val node = GridPane()

                    node.add(shortWideLabel(displayText),0,0)

                    this.graphic = node
                }
            } else {
                this.graphic = shortWideLabel("")
                this.padding = Insets.EMPTY
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
        val retval = proportionalButton(text, action, specialWidth)
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
        var width = proportion
        if(proportion > 1.0){
            width = 1/proportion
        }

        val retval = TextField(text)
        setSize(retval, proportion)
        retval.style = "-fx-background-image: url(assets/general/generalTextField.png); -fx-background-repeat: stretch; -fx-background-size: ${UIGlobals.totalWidth() * width} ${UIGlobals.totalHeight() * 0.1};"
        retval.alignment = Pos.CENTER
        return retval
    }

    fun extraShortWideLabel(text: String): Node {
        val retval = Label(text)
        setSize(retval, 1.0)
        retval.minHeight = UIGlobals.totalHeight()/20
        retval.alignment = Pos.CENTER
        return retval
    }

    fun shortWideLabel(text: String): StackPane{
        return shortProportionalLabel(text, 1.0)
    }

    fun shortProportionalLabel(text: String, proportion: Double): StackPane{
        return proportionalLabel(text, proportion, 0.1)
    }

    fun proportionalLabel(text: String, proportion: Double, height: Double): StackPane{
        //temporary workaround to handle the old way proportions worked
        var width = proportion
        if(proportion > 1.0){
            width = 1/proportion
        }

        val text = Label(text)
        setSize(text, proportion)
        text.font = Font(13.0)
        text.alignment = Pos.CENTER
        text.padding = Insets(15.0, 0.0, 15.0, 0.0)
        text.isWrapText = true
        text.textAlignment = TextAlignment.JUSTIFY
        text.maxWidth = UIGlobals.totalWidth() * width

        val retval = StackPane()

        val background = imageView("assets/general/generalInfo.png", height, proportion)

        retval.children.add(background)
        retval.children.add(text)

        return retval
    }

    fun proportionalButton(text: String, action: EventHandler<MouseEvent>?, proportion: Double): Node{
        return proportionalButton(text, action, proportion, 0.1, false)
    }

    fun proportionalButton(text: String, action: EventHandler<MouseEvent>?, width: Double, height: Double, clicked: Boolean): Node{
        val image = if(clicked){ imageView("assets/general/generalButtonSelected.png", height, width)} else {imageView("assets/general/generalButton.png", height, width)}

        val text = Text(text)
        if(clicked){
            text.font = Font(19.0)
        } else {
            text.font = Font(20.0)
        }

        val retval = StackPane()
        retval.children.add(image)
        retval.children.add(text)
        if (action != null) {
            retval.onMouseClicked = EventHandler { event -> if(UIGlobals.GUI().lastButtonClicked != null) {(UIGlobals.GUI().lastButtonClicked as StackPane).children.set(0, imageView("assets/general/generalButton.png", height, width))};
                UIGlobals.GUI().lastButtonClicked = retval;
                retval.children.set(0, imageView("assets/general/generalButtonSelected.png", height, width));
                image.fitWidth = UIGlobals.totalWidth() / width; action.handle(event) }
        }
        return retval
    }

    fun setButtonDisable(button: Node){
        val buttonAsStack = button as StackPane
        val oldImage = buttonAsStack.children[0] as ImageView
        buttonAsStack.children[0] = imageView("assets/general/generalButtonDisabled.png", oldImage.fitHeight / UIGlobals.totalHeight(), UIGlobals.totalWidth()/oldImage.fitWidth)
        buttonAsStack.onMouseClicked = EventHandler {  }
    }

    private fun setSize(node: Control, proportion: Double){
        //temporary workaround to handle the old way proportions worked
        var width = proportion
        if(proportion > 1.0){
            width = 1/proportion
        }

        node.setMinSize(UIGlobals.totalWidth() * width, UIGlobals.totalHeight() / 10)
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
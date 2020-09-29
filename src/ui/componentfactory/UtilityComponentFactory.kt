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
import javafx.scene.layout.Pane
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
import ui.specialdisplayables.NewSceneSelector
import ui.specialdisplayables.selectionmodal.Tab
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import java.io.File

object UtilityComponentFactory {

    const val paper_background = "url(assets/general/generalInfo.png)"

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

    val imageBuffer = mutableMapOf<String, Image>()

    fun imageFromPath(url: String): Image{
        if(UIGlobals.guiOrNull() == null){
            return WritableImage(1,1)
        }
        if(imageBuffer.containsKey(url)){
            return imageBuffer[url]!!
        }
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

    fun <T: Describable> basicList(items: List<T>, onClick: (T) -> Unit, removeAction: ((T) -> Unit)?, width: Double, height: Double): ListView<T> {
        val data = FXCollections.observableArrayList<T>()
        data.addAll(items)
        val listView = ListView<T>(data)
        listView.style = "-fx-focus-color:rgba(0,0,0,0.0);  -fx-padding: 0; -fx-background-image: $paper_background"
        listView.items = data
        listView.setPrefSize(width,height)
        listView.setCellFactory({ _: ListView<T> -> ActionPickCell(onClick, removeAction) })

        return listView
    }

    internal class ActionPickCell<T: Describable>(val closeAction: ((T) -> Unit)?, val removeAction: ((T) -> Unit)?) : ListCell<T>() {
        public override fun updateItem(item: T?, empty: Boolean) {
            if(item != null){
                super.updateItem(item, empty)
                val displayText = item.tooltip(UIGlobals.playingAs())

                val node = Pane()
                if(closeAction != null){
                    val primaryButton = shortWideButton(displayText, EventHandler { _ -> this.graphic = shortWideClickedButton(displayText, EventHandler { });  Platform.runLater { closeAction!!(item) }})
                    primaryButton.setOnMouseEntered { event -> if(event.clickCount > 0){ closeAction!!(item) } }
                    node.children.add(primaryButton)
                    this.padding = Insets.EMPTY
                } else {
                    node.children.add(shortWideLabel(displayText))
                }

                if(removeAction != null){
                    val cancelButton = imageView("assets/general/removeItemButton.png", 0.08, 0.07)
                    cancelButton.setOnMouseClicked { _ -> removeAction!!(item) }
                    cancelButton.setOnMouseEntered { event -> if(event.clickCount > 0){ removeAction!!(item) } }
                    cancelButton.x = UIGlobals.totalWidth() * 0.9
                    node.children.add(cancelButton)
                }

                this.graphic = node
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
        text.padding = Insets(15.0, 15.0, 15.0, 15.0)
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

    val behaviorByButton = mutableMapOf<Node, EventHandler<MouseEvent>?>()

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
        if(action != null){
            behaviorByButton[retval] = EventHandler { event ->
                UIGlobals.playSound("click.wav")
                if(UIGlobals.GUI().lastButtonClicked != null) {
                    UIGlobals.clearLastSelectedButton()
                }
                UIGlobals.GUI().lastButtonClicked = retval
                retval.children.set(0, imageView("assets/general/generalButtonSelected.png", height, width))
                image.fitWidth = UIGlobals.totalWidth() / width
                Platform.runLater {
                    action.handle(event)
                }
            }
        }
        retval.onMouseClicked = behaviorByButton.getOrDefault(retval, null)
            return retval
    }

    fun setButtonDisable(button: Node, disabled: Boolean){
        val buttonAsStack = button  as StackPane
        val oldImage = buttonAsStack.children[0] as ImageView

        if(disabled){
            buttonAsStack.children[0] = imageView("assets/general/generalButtonDisabled.png", oldImage.fitHeight / UIGlobals.totalHeight(), oldImage.fitWidth/UIGlobals.totalWidth())
            buttonAsStack.onMouseClicked = EventHandler {  }
        } else {
            buttonAsStack.children[0] = imageView("assets/general/generalButton.png", oldImage.fitHeight / UIGlobals.totalHeight(), oldImage.fitWidth/UIGlobals.totalWidth())
            buttonAsStack.onMouseClicked = behaviorByButton[button]
        }

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
        val recentReportTab = Tab("Reports from this Turn", perspective.player.recentReports(UIGlobals.activeGame().turn))
        val oldReportsTab = Tab("All Reports", perspective.player.knownReports.sortedByDescending { it.turn })

        return listOf(recentReportTab, oldReportsTab)
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
        retval.onMouseClicked =  EventHandler { _ -> UIGlobals.playSound("click.wav"); action()}
        return retval
    }

}
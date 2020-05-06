package ui.specialdisplayables.worldgen

import javafx.event.EventHandler
import javafx.geometry.Rectangle2D
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import ui.NoPerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory
import kotlin.math.max
import kotlin.math.min

class WorldEditorMainMenu: NoPerspectiveDisplayable() {

    val baseWidth = UIGlobals.totalWidth()
    val baseHeight = UIGlobals.totalHeight()

    val imageView = UtilityComponentFactory.imageView("maps/testland/background.png")

    var focusX = 1000.0
    var focusY = 750.0
    var zoom = 0.5

    override fun display(): Scene {
        val pane = GridPane()

        setViewPort()
        imageView.onScroll = EventHandler { event -> changeViewport(event.x - baseWidth/2.0,event.y - (baseHeight*0.8)/2.0, 0.01 * event.deltaY) }
        pane.add(imageView, 0, 0)

        val bottomPane = GridPane()
        bottomPane.add(UtilityComponentFactory.proportionalBackButton(2.0), 0, 0)
        bottomPane.add(UtilityComponentFactory.proportionalButton("Save Map", EventHandler { _ -> println("saving map")},2.0), 1, 0)

        pane.add(bottomPane, 0, 11)

        return Scene(pane)
    }

    private fun changeViewport(x: Double, y: Double, scaleDelta: Double){
        val zoomDelta = max(min(scaleDelta, 1.0), -1.0)
        if(zoomDelta > 0){
            focusX += x/(3*zoom)
            focusY += y/(3*zoom)
        }
        zoom += zoomDelta
        setViewPort()
    }

    private fun setViewPort(){
        if(zoom < 0 || (baseWidth / zoom > imageView.image.width || baseHeight / zoom > imageView.image.height)){
            zoom = min(baseWidth / imageView.image.width, baseHeight / imageView.image.height)
        }
        if(zoom > 10.0){
            zoom = 10.0
        }
        val displayWidth = baseWidth/zoom
        val displayHeight = baseHeight/zoom

        if(focusX - (displayWidth/2) < 0){
            focusX = displayWidth / 2
        }
        if(focusX + (displayWidth/2) > imageView.image.width){
            focusX = imageView.image.width- (displayWidth / 2)
        }
        if(focusY - (displayHeight/2) < 0){
            focusY = displayHeight / 2
        }
        if(focusY + (displayHeight/2) > imageView.image.height){
            focusY = imageView.image.height- (displayHeight / 2)
        }

        imageView.viewport = Rectangle2D(focusX - (displayWidth/2.0),focusY - (displayHeight/2.0), displayWidth, displayHeight)
    }
}
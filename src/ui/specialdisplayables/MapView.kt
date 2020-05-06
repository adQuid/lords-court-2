package ui.specialdisplayables

import javafx.event.EventHandler
import javafx.geometry.Rectangle2D
import javafx.scene.Node
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import main.UIGlobals
import ui.componentfactory.UtilityComponentFactory
import kotlin.math.max
import kotlin.math.min

class MapView {
    val baseWidth = UIGlobals.totalWidth()
    val baseHeight = UIGlobals.totalHeight()

    val layers: List<MapLayer>
    val backgroundName = "/background.png"
    val territoriesName = "/territories.png"

    var focusX: Double
    var focusY: Double
    var zoom = 0.5

    constructor(url: String, x: Double, y: Double){
        focusX = x
        focusY = y
        layers = listOf(
            MapLayer(UtilityComponentFactory.imageView(url+backgroundName), true),
            MapLayer(UtilityComponentFactory.imageView(url+territoriesName), true)
        )
    }

    fun display(): Node {
        val retval = GridPane()
        setViewPort()
        layers[0].imageView.onScroll = EventHandler { event -> changeViewport(event.x - baseWidth/2.0,event.y - (baseHeight*0.8)/2.0, 0.01 * event.deltaY) }
        layers.forEach {
            if(it.active){
                retval.add(it.imageView,0,0)
            }
        }
        return retval
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
        if(zoom < 0 || (baseWidth / zoom > layers[0].imageView.image.width || baseHeight / zoom > layers[0].imageView.image.height)){
            zoom = min(baseWidth / layers[0].imageView.image.width, baseHeight / layers[0].imageView.image.height)
        }
        if(zoom > 10.0){
            zoom = 10.0
        }
        val displayWidth = baseWidth/zoom
        val displayHeight = baseHeight/zoom

        if(focusX - (displayWidth/2) < 0){
            focusX = displayWidth / 2
        }
        if(focusX + (displayWidth/2) > layers[0].imageView.image.width){
            focusX = layers[0].imageView.image.width- (displayWidth / 2)
        }
        if(focusY - (displayHeight/2) < 0){
            focusY = displayHeight / 2
        }
        if(focusY + (displayHeight/2) > layers[0].imageView.image.height){
            focusY = layers[0].imageView.image.height- (displayHeight / 2)
        }

        layers.forEach {
            it.imageView.viewport = Rectangle2D(focusX - (displayWidth/2.0),focusY - (displayHeight/2.0), displayWidth, displayHeight)
        }
    }

    class MapLayer{
        val imageView: ImageView
        val active: Boolean

        constructor(imageView: ImageView, active: Boolean) {
            this.imageView = imageView
            this.active = active
        }
    }
}
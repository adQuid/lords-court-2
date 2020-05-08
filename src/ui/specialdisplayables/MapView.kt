package ui.specialdisplayables

import javafx.event.EventHandler
import javafx.geometry.Rectangle2D
import javafx.scene.Node
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import main.UIGlobals
import ui.componentfactory.UtilityComponentFactory
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class MapView {
    val baseWidth = UIGlobals.totalWidth()
    val baseHeight = UIGlobals.totalHeight()

    val background: MapLayer
    val territories: MapLayer
    val annotations: MapLayer

    var focusX: Double
    var focusY: Double
    var zoom = 1.0

    var onClick = {x: Double, y: Double -> selectArea(x,y)}

    constructor(url: String, x: Double, y: Double){
        focusX = x
        focusY = y
        background =  MapLayer(UtilityComponentFactory.imageView(url+"/background.png"), true)
        territories = MapLayer(UtilityComponentFactory.imageView(url+"/territories.png"), true)
        annotations = MapLayer(UtilityComponentFactory.writableImageView(), true)
    }

    fun display(): Node {
        val retval = GridPane()
        setViewPort()
        background.imageView.onScroll = EventHandler { event -> changeViewport(event.x - baseWidth/2.0,event.y - (baseHeight*0.8)/2.0, 0.005 * event.deltaY) }
        background.imageView.onMouseClicked = EventHandler { event -> onClick(clickedOnX(event.x),clickedOnY(event.y)) }
        allLayers().forEach {
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
        if(zoom < 0 || (baseWidth / zoom > background.imageView.image.width || baseHeight / zoom > background.imageView.image.height)){
            zoom = min(baseWidth / background.imageView.image.width, baseHeight / background.imageView.image.height)
        }
        if(zoom > 10.0){
            zoom = 10.0
        }

        if(focusX - (displayWidth()/2) < 0){
            focusX = displayWidth() / 2
        }
        if(focusX + (displayWidth()/2) > background.imageView.image.width){
            focusX = background.imageView.image.width- (displayWidth() / 2)
        }
        if(focusY - (displayHeight()/2) < 0){
            focusY = displayHeight() / 2
        }
        if(focusY + (displayHeight()/2) > background.imageView.image.height){
            focusY = background.imageView.image.height- (displayHeight() / 2)
        }

        allLayers().forEach {
            it.imageView.viewport = Rectangle2D(focusX - (displayWidth()/2.0),focusY - (displayHeight()/2.0), displayWidth(), displayHeight())
        }
    }

    private fun allLayers(): List<MapLayer> {
        return listOf(background, territories, annotations)
    }

    private fun displayWidth(): Double {
        return baseWidth/zoom
    }

    private fun displayHeight(): Double {
        return baseHeight/zoom
    }

    private fun clickedOnX(x: Double): Double {
        return (x/zoom) + max(0.0,focusX - (displayWidth()/2.0))
    }

    private fun clickedOnY(y: Double): Double {
        return (y/zoom) + max(0.0,focusY - ((displayHeight()*0.8)/2.0))
    }

    private fun selectArea(x: Double, y: Double){
        annotations.imageView.image = WritableImage(UIGlobals.totalWidth().toInt(), ((UIGlobals.totalHeight()) * (8.0 / 10.0)).toInt())
        adjacentPixels(x, y).forEach {
            (annotations.imageView.image as WritableImage).pixelWriter.setColor(it.first, it.second, Color.BEIGE)
        }
    }

    private fun adjacentPixels(x: Double, y: Double): List<Pair<Int,Int>> {
        val retval = mutableListOf(Pair(x.roundToInt(), y.roundToInt()))
        val toCheck = mutableSetOf(Pair(x.roundToInt(),y.roundToInt()))
        val alreadyChecked = mutableListOf<Pair<Int,Int>>()

        while(toCheck.isNotEmpty()){
            val next = toCheck.first()

            if(!alreadyChecked.contains(next)){
                if(territories.imageView.image.pixelReader.getArgb(next.first, next.second) == 0){
                    retval.add(next)
                    toCheck.add(Pair(next.first-1, next.second))
                    toCheck.add(Pair(next.first+1, next.second))
                    toCheck.add(Pair(next.first, next.second-1))
                    toCheck.add(Pair(next.first, next.second+1))
                }
                alreadyChecked.add(next)
            }

            if(alreadyChecked.size > 10000){
                println("too big!")
                return retval
            }
            toCheck.remove(next)
        }
        return retval
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
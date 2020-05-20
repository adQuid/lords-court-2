package ui.specialdisplayables

import game.gamelogicmodules.territory.Territory
import game.gamelogicmodules.territory.TerritoryMap
import javafx.event.EventHandler
import javafx.geometry.Rectangle2D
import javafx.scene.Node
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.input.ScrollEvent
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

    //TODO: merge with values from above
    var widthSize: Double
    var heightSize: Double

    val map: TerritoryMap
    var background: MapLayer
    var territories: MapLayer
    var secondaryAnnotations: MapLayer
    var annotations: MapLayer

    var focusX: Double
    var focusY: Double
    var zoom = 1.0

    var onClick = {x: Double, y: Double -> println(selectTerritoryAt(x,y, true, false))}

    constructor(map: TerritoryMap, widthSize: Double, heightSize: Double){
        this.map = map
        focusX = 0.0
        focusY = 0.0
        this.widthSize = widthSize
        this.heightSize = heightSize
        background =  MapLayer(UtilityComponentFactory.imageView(map.imageUrl+"/background.png", heightSize), true)
        territories = MapLayer(UtilityComponentFactory.imageView(map.imageUrl+"/territories.png", heightSize), true)
        secondaryAnnotations = MapLayer(UtilityComponentFactory.writableImageView(heightSize), true)
        annotations = MapLayer(UtilityComponentFactory.writableImageView(heightSize), true)
    }

    fun resize(widthSize: Double, heightSize: Double){
        this.focusY -= (displayHeight() - baseHeight*heightSize/zoom)/2
        this.focusX -= (displayWidth() - baseWidth*widthSize/zoom)/2
        this.widthSize = widthSize
        this.heightSize = heightSize
        background =  MapLayer(UtilityComponentFactory.imageView(map.imageUrl+"/background.png", heightSize), true)
        territories = MapLayer(UtilityComponentFactory.imageView(map.imageUrl+"/territories.png", heightSize), true)
        secondaryAnnotations = MapLayer(UtilityComponentFactory.writableImageView(heightSize), true)
        annotations = MapLayer(UtilityComponentFactory.writableImageView(heightSize), true)
    }

    fun display(): Node {
        val retval = GridPane()
        setViewPort()
        val scrollHandler = EventHandler<ScrollEvent> { event -> changeViewport(event.x -(baseWidth*widthSize)/2.0,event.y - (baseHeight*heightSize)/2.0, 0.005 * event.deltaY) }
        background.imageView.onScroll = scrollHandler
        territories.imageView.onScroll = scrollHandler
        secondaryAnnotations.imageView.onScroll = scrollHandler
        annotations.imageView.onScroll = scrollHandler
        background.imageView.onMouseClicked = EventHandler { event -> onClick(clickedOnX(event.x),clickedOnY(event.y)) }
        secondaryAnnotations.imageView.onMouseClicked = EventHandler { event -> onClick(clickedOnX(event.x),clickedOnY(event.y)) }
        annotations.imageView.onMouseClicked = EventHandler { event -> onClick(clickedOnX(event.x),clickedOnY(event.y)) }
        allLayers().forEach {
            if(it.active){
                retval.add(it.imageView,0,0)
            }
        }
        refresh()
        return retval
    }

    fun refresh(){
        UtilityComponentFactory.refreshImageView(annotations.imageView)
        populateCapitals()
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
        return listOf(background, territories, secondaryAnnotations, annotations)
    }

    private fun displayWidth(): Double {
        return baseWidth*widthSize/zoom
    }

    private fun displayHeight(): Double {
        return baseHeight*heightSize/zoom
    }

    private fun clickedOnX(x: Double): Double {
        return (x/zoom) + focusX - (displayWidth()/2.0)
    }

    private fun clickedOnY(y: Double): Double {
        return (y/zoom) + focusY - ((displayHeight())/2.0)
    }

    fun selectTerritoryAt(x: Double, y: Double, highlight: Boolean, makeNew: Boolean): Territory?{
        refresh()
        val area = adjacentPixels(x,y)
        if(map.territories.filter { Pair(it.x,it.y) in area }.isNotEmpty()){
            if(highlight){
                highlightArea(annotations, area, Color(0.9607843, 0.9607843, 0.8627451, 0.5))
            }
            return map.territories.filter { Pair(it.x,it.y) in area }.first()
        } else if(makeNew){
            val newTer = Territory(map.nextId++, "Unnamed", x.roundToInt(), y.roundToInt())
            map.territories.add(newTer)
            if(highlight){
                highlightArea(annotations, area, Color(0.9607843, 0.9607843, 0.8627451, 0.5))
            }
            return newTer
        } else {
            if(highlight){
                highlightArea(annotations, area, Color(0.9607843, 0.9607843, 0.8627451, 0.5))
            }
            return null
        }
    }

    fun secondaryHighlight(territories: Collection<Territory>){
        highlightArea(secondaryAnnotations, territories.map { adjacentPixels(it.x.toDouble(), it.y.toDouble())}.flatten(), Color(0.2607843, 0.9607843, 0.2627451, 0.7))
    }

    private fun highlightArea(layer: MapLayer, coords: List<Pair<Int,Int>>, color: Color){
        coords.forEach {
            if(layer.imageView.image.pixelReader.getArgb(it.first, it.second) == 0){
                (layer.imageView.image as WritableImage).pixelWriter.setColor(it.first, it.second, color)
            }
        }
    }

    private fun adjacentPixels(x: Double, y: Double): List<Pair<Int,Int>> {
        val retval = mutableListOf<Pair<Int,Int>>()
        val toCheck = mutableSetOf(Pair(x.roundToInt(),y.roundToInt()))
        val alreadyChecked = mutableListOf<Pair<Int,Int>>()

        while(toCheck.isNotEmpty()){
            val next = toCheck.first()

            if(!alreadyChecked.contains(next)){
                if(next.first > 0 && next.second > 0 && territories.imageView.image.pixelReader.getArgb(next.first, next.second) == 0){
                    retval.add(next)
                    toCheck.add(Pair(next.first-1, next.second))
                    toCheck.add(Pair(next.first+1, next.second))
                    toCheck.add(Pair(next.first, next.second-1))
                    toCheck.add(Pair(next.first, next.second+1))
                }
                alreadyChecked.add(next)
            }

            if(alreadyChecked.size > 5000){
                println("too big!")
                return retval
            }
            toCheck.remove(next)
        }
        return retval
    }

    private fun populateCapitals(){
        map.territories.forEach {
            (annotations.imageView.image as WritableImage).pixelWriter.setColor(it.x, it.y, Color.BLACK)
            (annotations.imageView.image as WritableImage).pixelWriter.setColor(it.x-1, it.y, Color.BLACK)
            (annotations.imageView.image as WritableImage).pixelWriter.setColor(it.x+1, it.y, Color.BLACK)
            (annotations.imageView.image as WritableImage).pixelWriter.setColor(it.x, it.y-1, Color.BLACK)
            (annotations.imageView.image as WritableImage).pixelWriter.setColor(it.x, it.y+1, Color.BLACK)
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
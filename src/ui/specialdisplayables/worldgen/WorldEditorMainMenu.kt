package ui.specialdisplayables.worldgen

import com.beust.klaxon.Klaxon
import com.google.gson.Gson
import game.gamelogicmodules.territory.Territory
import game.gamelogicmodules.territory.TerritoryMap
import javafx.event.EventHandler
import javafx.geometry.Rectangle2D
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import ui.NoPerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory
import ui.specialdisplayables.MapView
import java.io.File
import kotlin.math.max
import kotlin.math.min

object WorldEditorMainMenu: NoPerspectiveDisplayable() {

    val mapName = "maps/testland"

    val pane = GridPane()

    val mapView = loadMap()

    val terNameDisplay = UtilityComponentFactory.shortProportionalTextField("No Territory Selected", 4.0)

    var selectedTerritory: Territory? = null

    override fun display(): Scene {
        terNameDisplay.isEditable = false
        mapView.onClick = {x,y -> selectedTerritory = mapView.selectTerritoryAt(x,y, true); terNameDisplay.isEditable = true;  terNameDisplay.text = selectedTerritory!!.name}
        pane.add(mapView.display(), 0, 0)

        val middlePane = GridPane()
        middlePane.add(terNameDisplay, 0, 0)
        middlePane.add(UtilityComponentFactory.shortButton("Place Territory Centers", EventHandler { _ -> println("what does this do?") }), 1, 0)
        middlePane.add(UtilityComponentFactory.shortButton("Clear Capital", EventHandler { _ -> clearCapital() }), 2, 0)
        middlePane.add(UtilityComponentFactory.shortButton("Save Territory", EventHandler { _ -> updateTerritory() }), 3, 0)
        pane.add(middlePane, 0,1)

        val bottomPane = GridPane()
        bottomPane.add(UtilityComponentFactory.proportionalBackButton(2.0), 0, 0)
        bottomPane.add(UtilityComponentFactory.proportionalButton("Save Map", EventHandler { _ -> saveMap()},2.0), 1, 0)
        pane.add(bottomPane, 0, 2)

        return Scene(pane)
    }

    private fun updateTerritory(){
        selectedTerritory!!.name = terNameDisplay.text
    }

    private fun clearCapital(){
        mapView.map.territories.remove(selectedTerritory!!)
        mapView.refresh()
    }

    private fun saveMap(){
        val gson = Gson()
        val saveFile = File(mapName+"/map.json")
        saveFile.writeText(gson.toJson(mapView.map.saveString()))
    }

    private fun loadMap(): MapView{
        val klac = Klaxon()
        val loadMap = klac.parse<Map<String,Any>>(File(mapName+"/map.json").readText())!!
        return MapView(TerritoryMap(loadMap), 1.0,0.8)
    }
}
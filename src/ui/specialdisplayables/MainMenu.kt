package ui.specialdisplayables

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import main.Controller
import main.UIGlobals
import ui.NoPerspectiveDisplayable
import ui.componentfactory.UtilityComponentFactory
import ui.specialdisplayables.selectionmodal.SelectionModal
import ui.specialdisplayables.selectionmodal.Tab
import ui.specialdisplayables.worldgen.WorldEditorMainMenu
import java.io.File
import java.nio.file.Files

class MainMenu: NoPerspectiveDisplayable() {

    override fun display(): Scene {
        val pane = GridPane()

        pane.add(UtilityComponentFactory.shortWideLabel("Welcome"), 0, 0)
        pane.add(UtilityComponentFactory.shortWideLabel("ba"), 0, 1)
        pane.add(UtilityComponentFactory.shortWideLabel("ba da ba ba ba ba da"), 0, 2)
        pane.add(UtilityComponentFactory.shortWideLabel("ba ba ba ba ba ba bahhhhh"), 0, 3)
        pane.add(UtilityComponentFactory.shortWideLabel("babababababababababa"), 0, 4)
        pane.add(UtilityComponentFactory.shortWideLabel("Filler"), 0, 5)
        pane.add(UtilityComponentFactory.shortWideLabel("Filler"), 0, 6)

        pane.add( UtilityComponentFactory.shortWideButton("Load", EventHandler { UIGlobals.focusOn(SelectionModal("Load Game", listOf(Tab("save files", saveGames())), { name -> Controller.singleton!!.load(name)}))}), 0, 7)

        pane.add(UtilityComponentFactory.shortWideButton("MAP EDITOR",  EventHandler { UIGlobals.focusOn(WorldEditorMainMenu) }), 0, 8)
        UtilityComponentFactory.setButtonDisable(pane.children[8], true)
        pane.add(UtilityComponentFactory.shortWideButton("PLAY NEW GAME", EventHandler {
            UIGlobals.focusOn(NewGameMenu())}), 0, 9)


        return Scene(pane)
    }
}

fun saveGames(): List<String>{
    return File("save/").walkTopDown().map { it.name }.filter{it.endsWith(".savgam")}.map { it.substringBeforeLast(".savgam") }.toList()
}
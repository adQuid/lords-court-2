package game.action

import game.Game
import game.GameCharacter
import game.Effect
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.text.Font
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.Describable
import ui.Displayable
import ui.componentfactory.UtilityComponentFactory

abstract class Action: Displayable, Describable {

    companion object{
        fun baseActionPane(action: Action): GridPane {
            val root = GridPane()

            val title = UtilityComponentFactory.shortWideLabel(action.tooltip(UIGlobals.playingAs()))
            title.font = Font(20.0)

            root.add(title,0,0)
            root.add(UtilityComponentFactory.shortWideLabel(action.description()),0,1)
            root.add(UtilityComponentFactory.backButton(),0,10)

            root.setPrefSize(UIGlobals.totalWidth(), UIGlobals.totalHeight())
            return root
        }
    }

    abstract fun doAction(game: Game, player: GameCharacter): List<Effect>

    abstract fun saveString(): Map<String, Any>

    abstract fun tooltip(perspective: ShortStateCharacter): String

    override fun universalDisplay(perspective: ShortStateCharacter?): Scene {
        return Scene(baseActionPane(this))
    }

    override fun hashCode(): Int {
        return 0 //So that sets are always comparing based on equals. There should never be huge collections of actions anyway
    }

    abstract fun collidesWith(other: Action): Boolean
}
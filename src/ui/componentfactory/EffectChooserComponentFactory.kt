package ui.componentfactory

import game.Effect
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import ui.Displayable

class EffectChooserComponentFactory {
    val context: List<Effect>

    constructor( context: List<Effect>){
        this.context = context
    }

    fun scenePage(perspective: ShortStateCharacter, action: (Effect) -> Unit): Scene {
        val root = GridPane()

        root.add(UtilityComponentFactory.basicList(context, action, UIGlobals.totalWidth(), (5* UIGlobals.totalHeight())/6), 0,0)
        root.add(UtilityComponentFactory.backButton(), 0,1)

        return Scene(root, UIGlobals.totalWidth(), UIGlobals.totalHeight())
    }
}

class EffectChooser: Displayable{
    val context: List<Effect>
    val callback: (Effect) -> Unit

    constructor(context: List<Effect>, action: (Effect) -> Unit){
        this.context = context
        this.callback = action
    }

    override fun display(perspective: ShortStateCharacter): Scene {
        return EffectChooserComponentFactory(context).scenePage(perspective, callback)
    }
}
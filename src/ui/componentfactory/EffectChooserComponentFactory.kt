package ui.componentfactory

import game.Effect
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import shortstate.ShortStateCharacter
import ui.Displayable
import ui.totalHeight
import ui.totalWidth

class EffectChooserComponentFactory {
    val context: List<Effect>

    constructor( context: List<Effect>){
        this.context = context
    }

    fun scenePage(perspective: ShortStateCharacter, action: (Effect) -> Unit): Scene {
        val root = GridPane()

        root.add(UtilityComponentFactory.basicList(context, action, totalWidth, (5* totalHeight)/6), 0,0)
        root.add(UtilityComponentFactory.backButton(), 0,1)

        return Scene(root, totalWidth, totalHeight)
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
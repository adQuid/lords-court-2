package ui.componentfactory

import game.Writ
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.room.action.DraftWrit
import ui.SoundLibrary
import ui.specialdisplayables.contructorobjects.WritConstructor

class WritComponentFactory {

    val writ: Writ


    constructor(parent: Writ){
        this.writ = parent
    }

    fun scenePage(perspective: ShortStateCharacter): Scene {
        val root = GridPane()

        root.add(UtilityComponentFactory.shortWideLabel(writ.name), 0,0)

        val description = UtilityComponentFactory.proportionalLabel(writ.deal.actions.map { entry -> entry.key.fullName()+" will "+entry.value.map{it.description()}.joinToString() }.joinToString(), 1.0, 0.5)
        root.add(description,0,1)

        root.add(UtilityComponentFactory.shortWideLabel("Signed by: ${writ.prettyPrintSignatories()}"),0,2)

        root.add(UtilityComponentFactory.shortWideButton("Details", EventHandler { _ -> UIGlobals.focusOn(writ.deal) }), 0,3)
        root.add(UtilityComponentFactory.shortWideButton("Modify (clears signatories)", EventHandler { _ -> perspective.player.writs.remove(writ); UIGlobals.defocus(); UIGlobals.focusOn(WritConstructor(writ.deal.toUnfinishedDeal())) }), 0,4)
        if(!perspective.player.writs.contains(writ)){
            UtilityComponentFactory.setButtonDisable(root.children[4], true)
        }
        val bottomPane = GridPane()
        if(perspective.player.writs.contains(writ) && !writ.signatories.contains(perspective.player)){
            bottomPane.add(UtilityComponentFactory.proportionalBackButton(0.5), 0, 0)
            bottomPane.add(UtilityComponentFactory.proportionalButton("Sign",
                EventHandler { writ.signatories.add(perspective.player); UIGlobals.playSound(SoundLibrary.write); UIGlobals.refresh() }, 0.5), 1, 0)
        } else {
            bottomPane.add(UtilityComponentFactory.backButton(),0,0)
        }

        root.add(bottomPane,0,5)

        val scene = Scene(root, UIGlobals.totalWidth(), UIGlobals.totalHeight())
        return scene
    }


}
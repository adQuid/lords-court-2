package ui.contructorobjects

import aibrain.UnfinishedDeal
import game.GameCharacter
import game.Writ
import javafx.scene.Scene
import shortstate.ShortStateCharacter
import ui.Displayable
import ui.componentfactory.WritConstructorComponentFactory

class WritConstructor: Displayable {
    companion object{
        const val COST_TO_MAKE_WRIT = 80
    }

    var name: String = "Untitled Writ"
    val deal: UnfinishedDeal

    val display = WritConstructorComponentFactory(this)

    constructor(deal: UnfinishedDeal){
        this.deal = deal
    }

    override fun display(perspective: ShortStateCharacter): Scene {
        return display.scenePage(perspective)
    }

    fun generateWrit(firstSigner: GameCharacter): Writ {
        if(deal.isEmpty()){
            throw Exception("Tried to make an empty Writ!")
        }
        return Writ(name, deal.toFinishedDeal(), listOf(firstSigner))
    }

    fun addWritToCharacter(character: ShortStateCharacter): Boolean{
        if(character.addEnergy(-COST_TO_MAKE_WRIT)){
            character.player.writs.add(generateWrit(character.player))
            return true
        }
        return false
    }
}
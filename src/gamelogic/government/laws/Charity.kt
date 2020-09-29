package gamelogic.government.laws

import aibrain.UnfinishedDeal
import gamelogic.government.Capital
import gamelogic.government.GovernmentLogicModule
import gamelogic.government.Law
import gamelogic.government.actionTypes.EnactLaw
import gamelogic.government.actionTypes.SetTaxRate
import gamelogic.resources.ResourceTypes
import gamelogic.territory.Territory
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import ui.Displayable
import ui.componentfactory.UtilityComponentFactory
import ui.specialdisplayables.contructorobjects.WritConstructor
import kotlin.math.min

class Charity: Law {
    companion object{
        val type = "charity"

        val ON_NEED_NAME = "onneed"
    }

    var onNeed = false
        get() = field
        private set

    constructor(onNeed: Boolean): super(Charity.type){
        this.onNeed = onNeed
    }

    constructor(other: Charity): this(other.onNeed)

    constructor(saveString: Map<String, Any>): super(saveString["type"] as String){
        onNeed = saveString[ON_NEED_NAME] as Boolean
    }

    override fun specialSaveString(): Map<String, Any>{
        return mapOf(ON_NEED_NAME to onNeed)
    }

    override fun apply(capital: Capital) {

        if(onNeed){
            val foodToEat = capital!!.territory!!.foodToEatNextTurn()
            val totalFood = foodToEat.resources.entries.sumBy { it.value } + capital!!.territory!!.resources.get(ResourceTypes.FLOUR_NAME)
            if(totalFood < capital!!.territory!!.foodNeededNextTurn()){
                val foodToGive = Territory.extractFood(capital!!.resources, capital!!.territory!!.resources.get(ResourceTypes.POPULATION_NAME) - totalFood)
                capital!!.resources.subtractAll(foodToGive)
                capital!!.territory!!.resources.addAll(foodToGive)

                //if enough direct foodstuffs cannot be found, give flour instead
                if((totalFood + foodToGive.resources.values.sum()) < capital!!.territory!!.foodNeededNextTurn()){
                    val flourToGive = min(capital!!.resources.get(ResourceTypes.FLOUR_NAME), capital!!.territory!!.foodNeededNextTurn() - (totalFood + foodToGive.resources.values.sum()))
                    capital!!.resources.add(ResourceTypes.FLOUR_NAME, -flourToGive)
                    capital!!.territory!!.resources.add(ResourceTypes.FLOUR_NAME, flourToGive)
                }
            }
        }
    }

    override fun constructorComponentFactory(capital: Capital): Displayable {
        return CharityConstructorFactory(this, capital)
    }

    override fun prettyPrint(context: ShortStateGame, perspective: ShortStateCharacter): String {
        return "Charity"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (this.onNeed != (other as Charity).onNeed) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }


    class CharityConstructorFactory: Displayable{

        val capital: Capital

        val charity: Charity
        val modCharity: Charity

        val draftButton = UtilityComponentFactory.shortWideButton("Draft Law", EventHandler { makeCharityFromAction() })

        constructor(charity: Charity, capital: Capital){
            this.capital = capital
            this.charity = charity
            this.modCharity = Charity(charity)
        }

        override fun universalDisplay(perspective: ShortStateCharacter?): Scene {
            update()

            val pane = GridPane()

            val onNeedPane = GridPane()
            onNeedPane.add(UtilityComponentFactory.shortProportionalLabel("Castle stocks will be used to provide food to the people where supplies are low among the people:", 0.5),0,0)
            onNeedPane.add(UtilityComponentFactory.shortButton("${if(modCharity.onNeed){"Yes"}else{"No"}}", EventHandler { modCharity.onNeed = !modCharity.onNeed; update(); UIGlobals.refresh()},0.5),1,0)

            pane.add(onNeedPane, 0,0)
            pane.add(UtilityComponentFactory.proportionalLabel("Filler", 1.0, 0.7),0,1)
            pane.add( draftButton,0,2)
            pane.add(UtilityComponentFactory.backButton(),0,3)
            return Scene(pane)
        }

        fun update(){
            UIGlobals.clearLastSelectedButton()
            if(charity == modCharity){
                UtilityComponentFactory.setButtonDisable(draftButton, true)
            } else {
                UtilityComponentFactory.setButtonDisable(draftButton, false)
            }
        }

        fun makeCharityFromAction(){
            makeWritFromAction(UIGlobals.playingAs(), modCharity, capital)
        }

    }
}
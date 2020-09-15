package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import aibrain.FinishedDeal
import game.action.Action
import shortstate.Conversation
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import shortstate.dialog.linetypes.traits.HasAction
import game.GameCharacter
import game.Game
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.dialog.GlobalLineTypeFactory
import ui.specialdisplayables.selectionmodal.SelectionModal
import ui.specialdisplayables.selectionmodal.Tab
import game.action.GlobalActionTypeFactory
import gamelogic.playerresources.PlayerResourceModule
import gamelogic.resources.ResourceTypes
import shortstate.ShortStateGame
import shortstate.room.Room

class AskAboutTradableGoods: Line {

    override val type: String
        get() = GlobalLineTypeFactory.ASK_GOODS_NAME

    constructor(){

    }

    constructor(saveString: Map<String, Any?>, game: Game){

    }

    override fun tooltipName(): String {
        return "Ask About Goods to Trade"
    }

    override fun symbolicForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("INQUIRE STOCKS"))
    }

    override fun fullTextForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "What do you have available to trade with me today?"
    }

    override fun tooltipDescription(): String {
        return "Check what resources the character has and would be willing to trade."
    }

    override fun specialSaveString(): Map<String, Any> {
        return hashMapOf()
    }

    override fun validToSend(): Boolean {
        return true
    }

    override fun canChangeTopic(): Boolean {
        return false
    }

    override fun possibleReplies(perspective: ShortStateCharacter, other: ShortStateCharacter, game: Game): List<Line> {
        return listOf(SimpleLine("I have ${fullList(perspective.player, game)}"))
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: ShortStateCharacter, game: Game): Line {
        return SimpleLine("I have ${fullList(brain.shortCharacter.player, game)}")
    }

    private fun fullList(me: GameCharacter, game: Game): String{
        val resourceModule = game.moduleOfType(PlayerResourceModule.type) as PlayerResourceModule

        return resourceModule.accessableResourcesForPlayer(game, me).resources.filter { it.key in ResourceTypes.tradableTypes }.map { "${it.value} ${it.key}" }.joinToString(", ")
    }
}
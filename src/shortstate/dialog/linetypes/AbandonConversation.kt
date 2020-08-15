package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import game.Game
import game.GameCharacter
import shortstate.Conversation
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import shortstate.room.Room
import shortstate.scenemaker.GoToRoomSoloMaker

class AbandonConversation: Line {

    override val type: String
        get() = GlobalLineTypeFactory.ABANDON_TYPE_NAME

    constructor(){

    }

    override fun tooltipName(): String {
        return "Nevermind"
    }

    override fun symbolicForm(context: ShortStateGame,  speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("Farewell"))
    }

    override fun fullTextForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "Sorry to bother you"
    }

    override fun tooltipDescription(): String {
        return "End this conversation."
    }

    override fun specialSaveString(): Map<String, Any> {
        return mapOf()
    }

    override fun validToSend(): Boolean {
        return true
    }

    override fun canChangeTopic(): Boolean {
        return true
    }

    override fun possibleReplies(perspective: ShortStateCharacter, other: ShortStateCharacter): List<Line> {
        return listOf()
    }

    override fun specialEffect(room: Room, shortGame: ShortStateGame, speaker: ShortStateCharacter) {
        if(shortGame.shortGameScene!!.conversation!!.lastLine is AbandonConversation){
            shortGame.shortGameScene!!.terminated = true
        }
        speaker.nextSceneIWannaBeIn = GoToRoomSoloMaker(speaker, room)
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: ShortStateCharacter, game: Game): Line {
        return AbandonConversation()
    }
}
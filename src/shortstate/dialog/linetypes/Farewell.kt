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

class Farewell: Line {

    override val type: String
        get() = GlobalLineTypeFactory.FAREWELL_TYPE_NAME

    constructor(){

    }

    override fun tooltipName(): String {
        return "Goodbye"
    }

    override fun symbolicForm(context: ShortStateGame,  speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("Farewell"))
    }

    override fun fullTextForm(context: ShortStateGame, speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "Goodbye"
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
        return false
    }

    override fun possibleReplies(perspective: ShortStateCharacter, other: ShortStateCharacter, game: Game): List<Line> {
        return listOf(Farewell())
    }

    override fun specialEffect(room: Room, shortGame: ShortStateGame, speaker: ShortStateCharacter) {
        if(shortGame.shortGameScene!!.conversation!!.lastLine is Farewell){
            shortGame.shortGameScene!!.terminated = true
        }
        speaker.nextSceneIWannaBeIn = GoToRoomSoloMaker(speaker, room)
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: ShortStateCharacter, game: Game): Line {
        return Farewell()
    }
}
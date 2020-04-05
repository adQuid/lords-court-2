package shortstate.dialog.linetypes

import aibrain.ConversationBrain
import game.Game
import game.GameCharacter
import shortstate.Conversation
import shortstate.ShortStateCharacter
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.dialog.Line
import shortstate.dialog.LineBlock
import shortstate.room.Room
import shortstate.scenemaker.GoToRoomSoloMaker

class Farewell: Line {

    override val type: String
        get() = GlobalLineTypeFactory.APPROVE_TYPE_NAME

    constructor(){

    }

    override fun tooltipName(): String {
        return "Goodbye"
    }

    override fun symbolicForm(speaker: ShortStateCharacter, target: ShortStateCharacter): List<LineBlock> {
        return listOf(LineBlock("Farewell"))
    }

    override fun fullTextForm(speaker: ShortStateCharacter, target: ShortStateCharacter): String {
        return "Goodbye"
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

    override fun possibleReplies(perspective: ShortStateCharacter): List<Line> {
        return listOf(Farewell())
    }

    override fun specialEffect(room: Room, conversation: Conversation, speaker: ShortStateCharacter) {
        speaker.nextSceneIWannaBeIn = GoToRoomSoloMaker(speaker, room)
    }

    override fun AIResponseFunction(brain: ConversationBrain, speaker: GameCharacter, game: Game): Line {
        return Farewell()
    }
}
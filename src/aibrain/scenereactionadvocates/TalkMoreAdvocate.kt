package aibrain.scenereactionadvocates

import shortstate.Conversation
import shortstate.ShortGameScene
import shortstate.ShortStateController
import shortstate.ShortStateGame
import shortstate.dialog.Line
import shortstate.dialog.linetypes.Farewell

class TalkMoreAdvocate: SceneReactionAdvocate {

    val me: game.GameCharacter

    constructor(character: game.GameCharacter) : super(character) {
        me = character
    }

    override fun weight(game: ShortStateGame, shortGameScene: ShortGameScene): Double {
        if(shortGameScene!!.conversation != null){
            if(shortGameScene!!.conversation!!.otherParticipant(shortGameScene!!.conversation!!.lastSpeaker).player.npc){
                if(!(nextLineIWouldSay(game, shortGameScene) is Farewell)){
                    return 4.0 - shortGameScene.conversation!!.age
                }
            }
            return 0.0
        }
        return 0.0
    }

    override fun doToScene(game: ShortStateGame, shortGameScene: ShortGameScene) {
        val convo = shortGameScene.conversation!!
        convo.submitLine(nextLineIWouldSay(game, shortGameScene), game)
    }

    private fun nextLineIWouldSay(game: ShortStateGame, shortGameScene: ShortGameScene): Line {
        val convo = shortGameScene.conversation!!
        if(convo.lastLine != null){
            return convo.lastLine!!.AIResponseFunction(convo.otherParticipant(convo.lastSpeaker).convoBrain, convo.lastSpeaker.player, game.game)
        } else {
            return convo.otherParticipant(convo.lastSpeaker).convoBrain.startConversation(convo.lastSpeaker.player, game.game)
        }
    }
}
package aibrain.scenereactionadvocates

import shortstate.ShortGameScene
import shortstate.ShortStateController

class TalkMoreAdvocate: SceneReactionAdvocate {

    val me: game.GameCharacter

    constructor(character: game.GameCharacter) : super(character) {
        me = character
    }

    override fun weight(shortGameScene: ShortGameScene): Double {
        if(shortGameScene!!.conversation != null){
            if(shortGameScene!!.conversation!!.otherParticipant(shortGameScene!!.conversation!!.lastSpeaker).player.npc){
                return 8.0 - shortGameScene!!.conversation!!.age
            }
            return 0.0
        }
        return 0.0
    }

    override fun doToScene(shortStateController: ShortStateController, shortGameScene: ShortGameScene) {
        val convo = shortGameScene.conversation!!
        if(convo.lastLine != null){
            convo.submitLine(convo.lastLine!!.AIResponseFunction(convo.otherParticipant(convo.lastSpeaker).convoBrain, convo.lastSpeaker.player, shortStateController.shortGame.game), shortStateController.shortGame)
        } else {
            convo.submitLine(convo.otherParticipant(convo.lastSpeaker).convoBrain.startConversation(convo.lastSpeaker.player, shortStateController.shortGame.game), shortStateController.shortGame)
        }
    }
}
package shortstate.room.action

import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import shortstate.dialog.linetypes.PresentPetition
import shortstate.room.Room
import shortstate.room.RoomAction
import shortstate.scenemaker.ConversationMaker
import shortstate.scenemaker.GoToRoomSoloMaker

class HoldAudience: RoomAction {

    constructor(){

    }

    override fun clickOn(game: ShortStateGame, player: ShortStateCharacter) {
        doActionIfCanAfford(game, player)
    }

    override fun cost(): Int {
        return 100
    }

    override fun doAction(game: ShortStateGame, player: ShortStateCharacter) {

        game.players.filter { it.nextSceneIWannaBeIn == null && it.player.npc}.forEach { it.decideNextScene(game) }

        //if any important characters want to talk to you, they get priority
        var charToTalkTo: ShortStateCharacter? = game.players.filter { it.nextSceneIWannaBeIn != null
                && it.nextSceneIWannaBeIn!! is ConversationMaker
                && (it.nextSceneIWannaBeIn!! as ConversationMaker).target == player }.firstOrNull()

        //if there's no existing characters, look for a petitioner
        if(charToTalkTo == null){
            charToTalkTo = game.players.filter { it.player.location == player.player.location  && it.player.petitions.isNotEmpty()}.firstOrNull()
        }


        if(charToTalkTo != null){
            //TODO: Put this into the controller
            game.shortGameScene = ConversationMaker(charToTalkTo, player, game.location.roomByType(Room.RoomType.THRONEROOM)).makeScene(game)
            charToTalkTo.nextSceneIWannaBeIn = null
            if(charToTalkTo.player.petitions.isNotEmpty()){
                game.shortGameScene!!.conversation!!.submitLine(PresentPetition(charToTalkTo.player.petitions.first()), game)
            }
            if(!player.player.npc){
                UIGlobals.resetFocus()
            }
        } else {
            if(!player.player.npc){
                UIGlobals.displayMessage("Nobody else comes forward.")
            }
        }
    }

    override fun defocusAfter(): Boolean {
        return false
    }

    override fun toString(): String {
        return "Await Petitioners"
    }
}
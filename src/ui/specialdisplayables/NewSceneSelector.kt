package ui.specialdisplayables

import main.Controller
import shortstate.ShortStateCharacter
import shortstate.room.Room
import shortstate.scenemaker.ConversationMaker
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker
import ui.NoPerspectiveDisplayable
import ui.PerspectiveDisplayable
import ui.specialdisplayables.selectionmodal.SelectionModal
import ui.specialdisplayables.selectionmodal.Tab

object NewSceneSelector {
    //kind of jenky, since this passes the perspective down. Will need to change if hotseat multiplayer ever becomes a thing
    fun newSceneSelector(perspective: ShortStateCharacter): NoPerspectiveDisplayable {
        return SelectionModal("New Scene",
            newSceneOptions(perspective),
            { maker -> goToNewSceneIfApplicable(maker, perspective) })
    }

    private fun newSceneOptions(perspective: ShortStateCharacter): List<Tab<SceneMaker>>{
        val goToRoomMakers = perspective.player.location.rooms.map { room -> GoToRoomSoloMaker(perspective, room) }
        val goToRoomTab = Tab<SceneMaker>("Go to Room", goToRoomMakers)

        val conversationMakers = Controller.singleton!!.shortThreadForPlayer(perspective).shortGame.players.minusElement(perspective)
            .map { player -> ConversationMaker(perspective, player,perspective.player.location.roomByType(
                Room.RoomType.ETC)) }
        val conversationTab = Tab<SceneMaker>("Conversation", conversationMakers)

        return listOf(goToRoomTab, conversationTab)
    }

    private fun goToNewSceneIfApplicable(maker: SceneMaker, perspective: ShortStateCharacter){
        val shortGame = Controller.singleton!!.shortThreadForPlayer(perspective).shortGame
        shortGame.shortPlayerForLongPlayer(perspective.player)!!.nextSceneIWannaBeIn = maker
        if(shortGame.shortGameScene != null){
            shortGame.shortGameScene!!.terminated = true
        }
    }
}
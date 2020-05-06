package ui.specialdisplayables

import main.Controller
import shortstate.ShortStateCharacter
import shortstate.room.Room
import shortstate.scenemaker.ConversationMaker
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker
import ui.PerspectiveDisplayable
import ui.specialdisplayables.selectionmodal.SelectionModal
import ui.specialdisplayables.selectionmodal.Tab

object NewSceneSelector {
    fun newSceneSelector(perspective: ShortStateCharacter): PerspectiveDisplayable{
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
        perspective.nextSceneIWannaBeIn = maker
        if(Controller.singleton!!.shortThread!!.shortGame.shortGameScene != null){
            Controller.singleton!!.shortThread!!.shortGame.shortGameScene!!.terminated = true
        }
    }
}
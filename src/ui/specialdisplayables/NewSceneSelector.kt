package ui.specialdisplayables

import main.Controller
import main.UIGlobals
import shortstate.ShortStateCharacter
import shortstate.room.Room
import shortstate.scenemaker.ConversationMaker
import shortstate.scenemaker.GoToRoomSoloMaker
import shortstate.scenemaker.SceneMaker
import ui.NoPerspectiveDisplayable
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
        val goToRoomMakers = perspective.player.location.rooms.filter{it.type in listOf(Room.RoomType.BEDROOM, Room.RoomType.ETC) || perspective.player.titles.isNotEmpty()}.map { room -> GoToRoomSoloMaker(perspective, room) }
        val goToRoomTab = Tab<SceneMaker>("Go to Room", goToRoomMakers)

        val conversationMakers = Controller.singleton!!.shortThreadForShortPlayer(perspective).shortGame.players
            .minusElement(perspective).filter { it.player.titles.isNotEmpty() }
            .map { player -> ConversationMaker(perspective, player,perspective.player.location.roomByType(
                Room.RoomType.ETC)) }.filter{perspective.energy >= it.cost()}
        val conversationTab =  Tab<SceneMaker>("Speak to Councillors", conversationMakers)

        val commonerConversationMakers = Controller.singleton!!.shortThreadForShortPlayer(perspective).shortGame.players
            .minusElement(perspective).filter { it.player.titles.isEmpty() && it.player.petitions.isEmpty() }
            .map { player -> ConversationMaker(perspective, player,perspective.player.location.roomByType(
                Room.RoomType.THRONEROOM)) }.filter{perspective.energy >= it.cost()}
        val commonerConversationTab = Tab<SceneMaker>("Speak to Commoners", commonerConversationMakers)


        return listOfNotNull(goToRoomTab, conversationTab, commonerConversationTab)
    }

    private fun goToNewSceneIfApplicable(maker: SceneMaker, perspective: ShortStateCharacter){
        if(perspective.energy < maker.cost()){
            UIGlobals.focusOn(Message("You can't afford this"))
            return
        }
        val shortGame = Controller.singleton!!.shortThreadForShortPlayer(perspective).shortGame
        shortGame.shortPlayerForLongPlayer(perspective.player)!!.nextSceneIWannaBeIn = maker
        if(shortGame.shortGameScene != null){
            shortGame.shortGameScene!!.terminated = true
        }
    }
}
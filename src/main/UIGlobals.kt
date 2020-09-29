package main

import game.Game
import game.GameCharacter
import game.action.Action
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane
import javafx.scene.media.Media
import shortstate.ShortStateCharacter
import shortstate.ShortStateGame
import ui.Displayable
import ui.MainUI
import ui.componentfactory.UtilityComponentFactory
import javafx.scene.media.MediaPlayer
import java.io.File

object UIGlobals {

    val mediaPlayers = mutableMapOf<String, MediaPlayer>()

    fun activeGame(): Game {
        return Controller.singleton!!.game!!
    }

    fun activeShortGame(): ShortStateGame {
        return Controller.singleton!!.shortThreadForShortPlayer(playingAs()).shortGame
    }

    fun guiOrNull(): MainUI? {
        return Controller.singleton!!.GUI
    }

    fun GUI(): MainUI {
        return Controller.singleton!!.GUI!!
    }

    fun playingAs(): ShortStateCharacter{
        return GUI().playingAs()!!
    }

    fun resetFocus(){
        if(guiOrNull() != null){
            GUI().resetFocus()
        }
    }

    fun refresh(){
        if(guiOrNull() != null){
            Controller.singleton!!.GUI!!.display()
        }
    }

    fun defocus(){
        if(guiOrNull() != null){
            GUI().defocus()
        }
    }

    fun focusOn(focus: Displayable?){
        if(guiOrNull() != null){
            GUI().focusOn(focus)
        }
    }

    fun specialFocusOn(focus: Displayable?){
        if(guiOrNull() != null){
            GUI().specialFocusOn(focus)
        }
    }

    fun playSound(filename: String){
        if(guiOrNull() != null){
            if(!mediaPlayers.containsKey(filename)){
                mediaPlayers[filename] = MediaPlayer(Media(File("sound/${filename}").toURI().toString()))
            }
            mediaPlayers[filename]!!.seek(mediaPlayers[filename]!!.startTime)
            mediaPlayers[filename]!!.play()
        }
    }

    fun appendActionsForPlayer(player: GameCharacter, actions: List<Action>){
        Controller.singleton!!.commitActionsForPlayer(player, actions)
    }

    fun clearLastSelectedButton(){
        if(GUI().lastButtonClicked != null){
            val buttonAsStack = GUI().lastButtonClicked!!  as StackPane
            val image = buttonAsStack.children[0] as ImageView
            (GUI().lastButtonClicked as StackPane).children.set(0,
                UtilityComponentFactory.imageView(
                    "assets/general/generalButton.png",
                    image.fitHeight / totalHeight(),
                    image.fitWidth / totalWidth()
                )
            )
        }
    }

    fun totalHeight(): Double{
        if(guiOrNull() != null){
            return GUI().totalHeight
        }
        return 0.0
    }

    fun totalWidth(): Double{
        if(guiOrNull() != null){
            return GUI().totalWidth
        }
        return 0.0
    }
}
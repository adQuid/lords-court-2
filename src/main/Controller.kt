package main

import game.action.Action
import aibrain.BrainThread
import com.beust.klaxon.Klaxon
import com.google.gson.Gson
import game.Game
import game.GameSetup
import game.GameCharacter
import javafx.application.Application
import shortstate.ShortGameScene
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import ui.MainUI
import java.io.File



class Controller {

    companion object {
        var singleton: Controller? = null
    }

    var game: Game? = null
    var shortThread: ShortStateController? = null
    var GUI: MainUI? = null

    private val brainThread1 = Thread(BrainThread(this))

    constructor(){
        singleton = this
        newGame()
    }

    fun sceneForPlayer(player: ShortStateCharacter): ShortGameScene?{
        return shortThread!!.shortGame!!.sceneForPlayer(player)
    }

    fun registerUI(gui: MainUI){
        GUI = gui
    }

    fun commitActionsForPlayer(player: GameCharacter, actions: List<Action>){
        game!!.commitActionsForPlayer(player, actions)
        if(game!!.actionsByPlayer.keys.size == game!!.players.size){
            println("ENDING TURN ${game!!.turn}")
            game!!.endTurn()
            val shortGame = ShortStateGame(game!!, game!!.locations[0])
            shortThread = ShortStateController(shortGame)
            Thread(shortThread).start()
            println("Deliciousness: ${game!!.deliciousness}")
        }
    }

    //TODO: Have save and load use the same library
    fun save(){
        val gson = Gson()
        val saveMap = mapOf<String, Any>(
            "game" to game!!.saveString(),
            "shortGame" to shortThread!!.shortGame!!.saveString()
        )

        val saveFile = File("save/test.savgam")
        saveFile.writeText(gson.toJson(saveMap))
    }

    //TODO: Have save and load use the same library
    fun load(){
        brainThread1.interrupt()

        val klac = Klaxon()
        val loadMap = klac.parse<Map<String,Any>>(File("save/test.savgam").readText())!!
        game = Game(loadMap["game"] as Map<String,Any>)
        val shortGame = ShortStateGame(game!!, loadMap["shortGame"] as Map<String, Any>)
        shortThread = ShortStateController(shortGame)
        startPlaying()
    }

    fun newGame(){
        game = Game()
        GameSetup().setupGame(game!!)
        val shortGame = ShortStateGame(game!!, game!!.locations[0])
        shortThread = ShortStateController(shortGame)
        startPlaying()
    }

    private fun startPlaying(){
        Thread(shortThread).start()
        brainThread1.start()
        Application.launch(MainUI::class.java)
    }
}

fun main() {
    Controller.singleton = Controller()
}
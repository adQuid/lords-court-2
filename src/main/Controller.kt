package main

import game.action.Action
import aibrain.BrainThread
import com.beust.klaxon.Klaxon
import com.google.gson.Gson
import game.Game
import game.GameSetup
import game.Character
import javafx.application.Application
import shortstate.Scene
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import ui.MainUI
import java.io.File



class Controller {

    companion object {
        var singleton: Controller? = null
    }

    var game: Game? = null
    var shortGame: ShortStateGame? = null
    var GUI: MainUI? = null

    private val brainThread1 = Thread(BrainThread(this))

    constructor(){
        singleton = this
        load()
    }

    fun sceneForPlayer(player: ShortStateCharacter): Scene?{
        return shortGame!!.sceneForPlayer(player)
    }

    fun registerUI(gui: MainUI){
        GUI = gui
    }

    fun commitActionsForPlayer(player: Character, actions: List<Action>){
        game!!.commitActionsForPlayer(player, actions)
        if(game!!.actionsByPlayer.keys.size == game!!.players.size){
            println("ENDING TURN ${game!!.turn}")
            game!!.endTurn()
            shortGame = ShortStateGame(game!!, game!!.locations[0])
            Thread(shortGame).start()
            println("Deliciousness: ${game!!.deliciousness}")
        }
    }

    //TODO: Have save and load use the same library
    fun save(){
        val gson = Gson()
        val saveMap = mapOf<String, Any>(
            "game" to game!!.saveString(),
            "shortGame" to shortGame!!.saveString()
        )

        val saveFile = File("save/test.savgam")
        saveFile.writeText(gson.toJson(saveMap))
        println(saveFile.absolutePath)
    }

    //TODO: Have save and load use the same library
    fun load(){
        val klac = Klaxon()
        val loadMap = klac.parse<Map<String,Any>>(File("save/test.savgam").readText())!!
        game = Game(loadMap["game"] as Map<String,Any>)
        shortGame = ShortStateGame(game!!, loadMap["shortGame"] as Map<String, Any>)
        startPlaying()
    }

    fun newGame(){
        game = Game()
        GameSetup().setupGame(game!!)
        shortGame = ShortStateGame(game!!, game!!.locations[0])
        Thread(shortGame).start()
        startPlaying()
    }

    private fun startPlaying(){
        brainThread1.start()
        Application.launch(MainUI::class.java)
    }
}

fun main() {
    Controller.singleton = Controller()
}
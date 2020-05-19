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
    var shortThreads = mutableListOf<ShortStateController>()
    var GUI: MainUI? = null

    private var brainThread1 = Thread(BrainThread(this))

    constructor(){
        singleton = this
    }

    fun shortThreadForPlayer(player: ShortStateCharacter): ShortStateController{
        return shortThreads.filter { it.shortGame.players.contains(player) }.first()
    }

    fun nextShortThread(): ShortStateController{
        return shortThreads.random() //TODO: make better
    }

    fun sceneForPlayer(player: ShortStateCharacter): ShortGameScene?{
        return shortThreadForPlayer(player).shortGame!!.sceneForPlayer(player)
    }

    fun registerUI(gui: MainUI){
        GUI = gui
    }

    fun commitActionsForPlayer(player: GameCharacter, actions: List<Action>){
        game!!.appendActionsForPlayer(player, actions)
    }

    fun concludeTurnForPlayer(player: GameCharacter){
        game!!.concludedPlayers.add(player)
        if(game!!.concludedPlayers.size == game!!.players.size){
            println("ENDING TURN ${game!!.turn}")
            game!!.endTurn()
            shortThreads.clear()

            game!!.locations.forEach {
                val shortGame = ShortStateGame(game!!, game!!.locations[0])
                val newThread = ShortStateController(shortGame)
                shortThreads.add(newThread)
                Thread(newThread).start()
            }
        }
    }

    //TODO: Have save and load use the same library
    fun save(){
        val gson = Gson()
        val saveMap = mapOf<String, Any>(
            "game" to game!!.saveString(),
            "shortGames" to shortThreads.forEach { it.shortGame!!.saveString()}
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
        val shortGames = loadMap["shortGame"] as List<Map<String, Any>>

        shortGames.forEach {
            shortThreads.add(ShortStateController(ShortStateGame(game!!, it)))
        }

        brainThread1 = Thread(BrainThread(this))

        startPlaying()
    }

    fun newGame(){
        game = GameSetup().setupAgricultureGame()

        game!!.locations.forEach {
            val shortGame = ShortStateGame(game!!, it)
            shortThreads.add(ShortStateController(shortGame))
        }
        UIGlobals.resetFocus()
        startPlaying()
    }

    fun startPlaying(){
        shortThreads.forEach {
            Thread(it).start()
        }
        brainThread1.start()
    }
}

fun main() {
    Controller.singleton = Controller()
    Application.launch(MainUI::class.java)
}
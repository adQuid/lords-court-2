package main

import game.action.Action
import aibrain.BrainThread
import com.beust.klaxon.Klaxon
import com.google.gson.Gson
import game.Game
import game.GameSetup
import game.GameCharacter
import game.Location
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

    private var brainThread1 = BrainThread(this)

    constructor(){
        singleton = this
    }

    fun shortThreadForPlayer(player: ShortStateCharacter): ShortStateController{
        return shortThreads.filter { it.shortGame.players.contains(player) }.first()
    }

    fun nextShortThread(): ShortStateController?{
        if(shortThreads.isNotEmpty()){
            return shortThreads.random() //TODO: make better
        } else {
            return null
        }
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

    @Synchronized fun concludeTurnForPlayer(player: GameCharacter){
        game!!.concludedPlayers.add(player)
        if(game!!.concludedPlayers.size == game!!.players.size){
            println("ENDING TURN ${game!!.turn}")
            game!!.endTurn()
            shortThreads.clear()

            populateShortThreads()
            runShortTreads()
        }
    }

    //TODO: Have save and load use the same library
    fun save(){
        val gson = Gson()
        val saveMap = mapOf<String, Any>(
            "game" to game!!.saveString(),
            "shortGames" to shortThreads.map { it.shortGame!!.saveString()}
        )

        val saveFile = File("save/test.savgam")
        saveFile.writeText(gson.toJson(saveMap))
    }

    //TODO: Have save and load use the same library
    fun load(){
        brainThread1.stopped = true

        val klac = Klaxon()
        val loadMap = klac.parse<Map<String,Any>>(File("save/test.savgam").readText())!!
        game = Game(loadMap["game"] as Map<String,Any>)
        val shortGames = loadMap["shortGames"] as List<Map<String, Any>>
        populateShortThreads()

        brainThread1 = BrainThread(this)

        startPlaying()
    }

    fun newGame(game: Game){
        this.game = game
        populateShortThreads()
        UIGlobals.resetFocus()
        startPlaying()
    }

    private fun populateShortThreads(){
        shortThreads.clear()
        game!!.locations.forEach {
            shortThreads.add(ShortStateController(ShortStateGame(game!!, it)))
        }
    }

    private fun runShortTreads(){
        shortThreads.forEach {
            Thread(it).start()
        }
    }

    fun startPlaying(){
        runShortTreads()
        Thread(brainThread1).start()
    }

    fun stopPlaying(){
        shortThreads.forEach {
            it.shortGame.stopped = true
        }
        brainThread1.stopped = true
    }
}

fun main() {
    Controller.singleton = Controller()
    Application.launch(MainUI::class.java)
}
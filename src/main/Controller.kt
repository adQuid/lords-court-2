package main

import game.action.Action
import aibrain.BrainThread
import com.beust.klaxon.Klaxon
import com.google.gson.Gson
import game.Game
import game.GameCharacter
import gamelogic.petitioners.SpecialAction
import gamelogic.territory.mapobjects.ShipType
import gamelogic.territory.mapobjects.StructureType
import javafx.application.Application
import shortstate.ShortGameScene
import shortstate.ShortStateGame
import shortstate.ShortStateCharacter
import shortstate.ShortStateController
import ui.MainUI
import java.io.File
import javax.swing.text.html.HTMLDocument


class Controller {

    companion object {
        var singleton: Controller? = null
    }

    var game: Game? = null
    private val shortThreads = mutableListOf<ShortStateController>()
        @Synchronized get() {return field}
    var GUI: MainUI? = null

    private var brainThread1 = BrainThread(this)

    constructor(){
        singleton = this
    }

    fun shortThreadForShortPlayer(player: ShortStateCharacter): ShortStateController{
        return shortThreads.filter { it.shortGame.players.contains(player) }.first()
    }

    fun shortThreadForPlayer(player: GameCharacter): ShortStateController{
        return shortThreads.filter { it.shortGame.shortPlayerForLongPlayer(player) != null }.first()
    }

    fun nextShortThread(): ShortStateController?{
        if(shortThreads.isNotEmpty()){
            return shortThreads.random() //TODO: make better
        } else {
            return null
        }
    }

    fun sceneForPlayer(player: ShortStateCharacter): ShortGameScene?{
        return shortThreadForShortPlayer(player).shortGame!!.sceneForPlayer(player)
    }

    fun registerUI(gui: MainUI){
        GUI = gui
    }

    fun commitActionsForPlayer(player: GameCharacter, actions: List<Action>){
        game!!.appendActionsForPlayer(player, actions)
    }

    fun concludeTurnForPlayer(player: GameCharacter){
        concludeTurnForPlayer(player, false)
    }

    @Synchronized fun concludeTurnForPlayer(player: GameCharacter, unconclude: Boolean){
        if(unconclude){
            game!!.concludedPlayers.remove(player)
        } else {
            game!!.concludedPlayers.add(player)
        }
        if(game!!.concludedPlayers.size == game!!.players.size){
            println("ENDING TURN ${game!!.turn}")
            game!!.endTurn()
            UIGlobals.playSound("end_turn.wav")
            shortThreads.clear()

            populateShortThreads()
            runThreads()
        }
    }

    //TODO: Have save and load use the same library
    fun save(name: String){
        val gson = Gson()
        val saveMap = mapOf<String, Any>(
            "game" to game!!.saveString(),
            "shortGames" to shortThreads.map { it.shortGame!!.saveString()}
        )

        val saveFile = File("save/${name}.savgam")
        saveFile.writeText(gson.toJson(saveMap))
    }

    //TODO: Have save and load use the same library
    fun load(name: String){
        brainThread1.stopped = true

        val klac = Klaxon()
        val loadMap = klac.parse<Map<String,Any>>(File("save/${name}.savgam").readText())!!
        game = Game(loadMap["game"] as Map<String,Any>)
        val shortGames = loadMap["shortGames"] as List<Map<String, Any>>
        shortThreads.clear()
        shortGames.forEach {
            shortThreads.add(ShortStateController(ShortStateGame(game!!, it)))
        }

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
        game!!.locations().forEach {
            shortThreads.add(ShortStateController(ShortStateGame(game!!, it)))
        }
    }

    private fun runThreads(){
        runPlayerThread()
        runAnotherThread()
    }

    fun runAnotherThread(){
        if(shortThreads.any { !it.started && it.shortGame.players.none { !it.player.npc } }){
            println("ran other thread")
            try{
                Thread(shortThreads.first{!it.started && it.shortGame.players.none { !it.player.npc }}).start()
            } catch (e: Exception){
                println("failed to run another thread. Perhaps they are all already running?")
            }
        }
    }

    //sort of a "poor man's concurrency" here: by putting this getter in the same singleton as the only modification, we can have multiple threads messing
    //with this without running onto concurrent modification exceptions
    fun playerThread(): ShortStateController{
        return shortThreads.filter { it.shortGame.players.filter { !it.player.npc }.isNotEmpty() }.first()
    }

    private fun runPlayerThread(){
        UIGlobals.resetFocus()
        if(shortThreads.any{!it.finished && it.shortGame.players.any { !it.player.npc }}){
            Thread(shortThreads.first{!it.finished && it.shortGame.players.any { !it.player.npc }}).start()
        }
    }

    private fun startPlaying(){
        setupRefData()
        runThreads()
        Thread(brainThread1).start()
    }

    fun stopPlaying(){
        shortThreads.forEach {
            it.shortGame.stopped = true
        }
        brainThread1.stopped = true
    }

    private fun setupRefData(){
        StructureType.loadStructureTypes()
        ShipType.loadShipTypes()
        SpecialAction.loadActionTypes()
    }

}

fun main() {
    Controller.singleton = Controller()
    Application.launch(MainUI::class.java)
}
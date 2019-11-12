package test.ai

import aibrain.ForecastBrain
import game.Character
import game.Location

fun testForcastBrainBasic(){
    val dummyLoc = Location()
    val character = Character("test name", "this doesn't matter", true, dummyLoc)
    val brain = ForecastBrain(character) //should I be doing this? The Character class already makes a brain for itself...


}
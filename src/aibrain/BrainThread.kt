package aibrain

import main.Controller

class BrainThread: Runnable {

    private val parent: Controller

    var stopped = false

    constructor(parent: Controller){
        this.parent = parent
    }

    override fun run() {
        while(true){
            if(stopped){
                return
            }
            val player = parent.game?.nextPlayerToForcast()

            if(player != null){
                player.brain.thinkAboutNextTurn(parent.game!!)
                println("turn forcast for $player")

            } else {
                val shortThread = parent.nextShortThread() //remember this uses random right now
                if(shortThread != null){
                    val shortPlayer = shortThread.shortGame.nextPlayerToDoShortStateStuff()

                    if(shortPlayer != null){
                        val scene = shortPlayer.nextSceneIWannaBeIn

                        if(scene == null){
                            shortPlayer.decideNextScene(shortThread.shortGame)
                        }
                    }
                }
                Thread.sleep(200)
            }
        }
    }
}
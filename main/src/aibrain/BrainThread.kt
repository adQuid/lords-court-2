package aibrain

import main.Controller

class BrainThread: Runnable {

    private val parent: Controller

    constructor(parent: Controller){
        this.parent = parent
    }

    override fun run() {
        while(true){
            val player = parent.game?.nextPlayerToForcast()

            if(player != null){
                player.brain.thinkAboutNextTurn(parent.game!!)
                println("turn forcast for $player")

            } else {
                val shortPlayer = parent.shortGame?.nextPlayerToDoShortStateStuff()

                if(shortPlayer == null){
                    Thread.sleep(200)
                    continue
                }

                println("forecasting next scene for ${shortPlayer.player}")
                val scene = shortPlayer.nextSceneIWannaBeIn

                if(scene == null){
                    shortPlayer.decideNextScene(parent.shortGame!!)
                }
                Thread.sleep(200)
            }


        }
    }
}
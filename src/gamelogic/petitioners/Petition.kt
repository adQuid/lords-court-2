package gamelogic.petitioners

import game.Game
import game.Writ
import shortstate.dialog.GlobalLineTypeFactory
import shortstate.dialog.Line

class Petition {

    val text: String
    val writ: Writ?

    constructor(text: String, writ: Writ?){
        this.text = text
        this.writ = writ
    }

    constructor(other: Petition){
        this.text = other.text
        if(other.writ != null){
            this.writ = Writ(other.writ)
        } else {
            this.writ = null
        }
    }

    constructor(saveString: Map<String, Any>, game: Game){
        this.text = saveString["line"] as String
        if(saveString["writ"] != "none"){
            this.writ = Writ(saveString["writ"] as Map<String, Any>, game)
        } else {
            this.writ = null
        }
    }

    fun saveString(): Map<String, Any>{
        if(writ != null){
            return mapOf(
                "line" to text,
                "writ" to writ!!.saveString()
            )
        } else {
            return mapOf(
                "line" to text,
                "writ" to "none"
            )
        }
    }

}
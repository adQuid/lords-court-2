package shortstate.dialog

import javafx.scene.paint.Color
import javafx.scene.text.Text
import shortstate.ShortStateCharacter

class LineBlock{

    val text: String
    val behavior: ((perspective: ShortStateCharacter) -> Unit)?
    var tabs = 0

    constructor(text: String): this(text, null)

    constructor(text: String, behavior: ((perspective: ShortStateCharacter) -> Unit)?){
        this.text = text
        this.behavior = behavior
    }

    fun tab(): LineBlock{
        tabs++
        return this
    }

    fun unTab(): LineBlock{
        tabs--
        return this
    }

    override fun toString(): String {
        return "  ".repeat(tabs) + text
    }

    fun textForm(perspective: ShortStateCharacter): Text {
        val retval = Text(toString())
        if(behavior != null){
            retval.setOnMouseClicked { _ -> behavior.invoke(perspective) }
            retval.fill = Color.DARKTURQUOISE
        }
        return retval
    }

}
package shortstate.dialog

import javafx.scene.paint.Color
import javafx.scene.text.Text
import shortstate.ShortStateCharacter

class LineBlock{

    val text: String
    val tooltip: String?
    val behavior: ((perspective: ShortStateCharacter) -> Unit)?
    var tabs = 0

    constructor(text: String): this(text, null, null)

    constructor(text: String, tooltip: String?): this(text, tooltip, null)

    constructor(text: String, behavior: ((perspective: ShortStateCharacter) -> Unit)?): this(text, null, behavior)

    constructor(text: String, tooltip: String?, behavior: ((perspective: ShortStateCharacter) -> Unit)?){
        this.text = text
        this.tooltip = tooltip
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
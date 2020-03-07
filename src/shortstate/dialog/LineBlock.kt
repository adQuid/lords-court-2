package shortstate.dialog

import javafx.scene.text.Text

class LineBlock{

    val text: String
    val behavior: (() -> Unit)?
    var tabs = 0

    constructor(text: String): this(text, null)

    constructor(text: String, behavior: (() -> Unit)?){
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

    fun textForm(): Text {
        val retval = Text(toString())
        if(behavior != null){
            retval.setOnMouseClicked { _ -> behavior.invoke() }
        }
        return retval
    }

}
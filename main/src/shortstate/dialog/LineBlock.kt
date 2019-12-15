package shortstate.dialog

class LineBlock{

    val text: String
    var tabs = 0

    constructor(text: String){
        this.text = text
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

}
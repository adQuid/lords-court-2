package dialog

abstract class Line {

    abstract fun symbolicForm(): List<LineBlock>

    abstract fun fullTextForm(): String

    abstract fun validToSend(): Boolean
}
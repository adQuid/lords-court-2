package ui.commoncomponents.selectionmodal

class Tab<T> {

    val title: String
    val items: List<T>

    constructor(title: String, items: List<T>){
        this.title = title
        this.items = items
    }

}
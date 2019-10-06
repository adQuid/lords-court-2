package shortstate

class Room {

    val name: String
    val pictureText: String

    constructor(name: String, pictureText: String){
        this.name = name
        this.pictureText = pictureText
    }

    override fun toString(): String {
        return name
    }

}
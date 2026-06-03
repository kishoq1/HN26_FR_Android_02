data class Player(
    val name : String,
    var stamina : Int = 100
)

fun Player.playMatch(){
    this.stamina -=20
    println("Van dong vien ${this.name} vua thi dau xong. The luc con lai: ${this.stamina}")
}

fun main(){
    val nameP = readlnOrNull() ?: ""
    val player = Player(nameP)
    player.playMatch()
}
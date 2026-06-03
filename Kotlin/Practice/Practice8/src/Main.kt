data class ServerLoad(var users : Int){
    operator fun plus(other: ServerLoad): ServerLoad{
        return ServerLoad(this.users + other.users)
    }
    operator fun inc() : ServerLoad{
        return ServerLoad(this.users+1)
    }
}

fun main(){
    var serverA = ServerLoad(100)
    var serverB = ServerLoad(60)
    val serverC = serverA + serverB
    println("${serverC.users}")
    serverA++
    println("${serverA.users}")
}

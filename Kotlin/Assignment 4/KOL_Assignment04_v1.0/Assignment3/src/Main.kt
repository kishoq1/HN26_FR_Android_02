data class HostConfig(
    val ip : String,
    var port : Int,
    val typeConnection: String,
)

fun displayList(list: List<HostConfig>){
    if(list.isEmpty()) println("No information")
    else{
        list.forEach { println("IP: ${it.ip}, Port: ${it.port}, Type connection: ${it.typeConnection}") }
    }
}

fun main(){
    val listHC = mutableListOf<HostConfig>()
    while (true){
        print("Input IP:")
        val ip = readlnOrNull() ?: ""
        print("Input port:")
        var port = readlnOrNull()?.toIntOrNull() ?: 0
        print("Input type connection:")
        val tc = readlnOrNull() ?: ""
        listHC.add(HostConfig(ip, port, tc))
        if(listHC.size >= 4){
            print("Do you wanna add more Host Config(y/n): ")
            val ans = readlnOrNull()?.lowercase()
            if(ans != "y") break
        }
    }

    println(" --- AUTO GENARATE INFORMATION ---")
    val expandedList = listHC.flatMap { config ->
        listOf(
            config,
            config.copy(port = config.port + 1),
            config.copy(port = config.port + 2),
            config.copy(port = config.port + 3)
        )
    }
    println("GENARATED INFORMATION!")
    println("--- DISPLAY LIST ---")
    while (true){
        println("Choose function to display. ")
        println("0. Exit")
        println("1. IP")
        println("2. Port")
        println("3. Type Connection")
        println("4. Host Config")
        println("5. <NONE> (Display all)")
        print("Your choice:")
        when(readlnOrNull()?.toIntOrNull()){
            0 -> {
                println("Exited program")
                break
            }
            1 -> {
                print("Input IP:")
                val searchIp = readlnOrNull() ?: ""
                displayList(expandedList.filter { it.ip == searchIp })
            }
            2 -> {
                print("Input Port: ")
                val searchPort = readlnOrNull()?.toIntOrNull() ?: 0
                displayList(expandedList.filter { it.port == searchPort })
            }
            3 -> {
                print("Input Type Connection:")
                val searchTC = readlnOrNull() ?: ""
                displayList(expandedList.filter { it.typeConnection == searchTC })
            }
            4 -> {
                println("Nhập thông tin Host Config cần tìm:")
                print("- IP: ")
                val sIp = readlnOrNull() ?: ""
                print("- Port: ")
                val sPort = readlnOrNull()?.toIntOrNull() ?: 0
                print("- Type: ")
                val sType = readlnOrNull() ?: ""
                displayList(expandedList.filter {
                    it.ip == sIp && it.port == sPort && it.typeConnection.equals(sType, ignoreCase = true)
                })
            }
            5 -> {
                displayList(expandedList)
            }
            else -> println("Choice invalid, please input from 0 to 5!")
        }
    }
}
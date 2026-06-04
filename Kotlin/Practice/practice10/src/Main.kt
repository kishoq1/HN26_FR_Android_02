data class CryptoAsset(
    val symbol : String,
    var curPrice : Double,
    val network : String
)

fun displayCrypto(list : List<CryptoAsset>){
    list.forEach { println("Coin : ${it.symbol}, Price: ${it.curPrice}, Network: ${it.network}") }
}

fun main(){
    val listCoin = mutableListOf<CryptoAsset>()
    while (true){
        print("Input coin symbol: ")
        val coin = readlnOrNull() ?: ""
        print("Input current price: ")
        val price = readlnOrNull()?.toDoubleOrNull() ?: 0.0
        print("Input network: ")
        val net = readlnOrNull() ?: ""
        listCoin.add(CryptoAsset(coin, price, net))
        if(listCoin.size >= 4){
            println("Do you wanna keep input crypto asset(y/n): ")
            val choice = readlnOrNull() ?: ""
            if (choice != "y") break
        }
    }
    println("--- AUTO GENARATE INFORMATION ---")
    val expandedList = listCoin.flatMap { coin -> listOf(
            coin,
            coin.copy(curPrice = coin.curPrice*1.1),
            coin.copy(curPrice = coin.curPrice*0.9)
        )
    }
    println("--- GENARATED INFOMATION! ---")
    while (true){
        println("Input choice to display")
        println("0. Exit")
        println("1. Symbol")
        println("2. Network")
        println("3. Price Under")
        println("4. Display all")
        when (readlnOrNull()?.toIntOrNull()){
            0 -> {
                println("Exited Program!")
                break;
            }
            1 -> {
                print("Input symbol: ")
                val symbol = readlnOrNull() ?: ""
                displayCrypto(expandedList.filter { it.symbol.equals(symbol, ignoreCase = true) })
            }
            2-> {
                print("Input Network: ")
                val nw = readlnOrNull() ?: ""
                displayCrypto(expandedList.filter { it.network.equals(nw, ignoreCase = true) })
            }
            3 -> {
                print("Input Price: ")
                val price = readlnOrNull()?.toDoubleOrNull() ?: 0.0
                displayCrypto(expandedList.filter { it.curPrice <= price })
            }
            4-> {
                displayCrypto(expandedList)
            }
            else -> println("Input invalid! please input number from 0 to 4.")
        }
    }
}
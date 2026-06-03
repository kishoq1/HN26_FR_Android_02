
val cryptoPrices = mapOf(
    "BTC" to 65000.0,
    "ETH" to 3500.0,
    "SOL" to 150.0
)

fun getPrice(coinName : String) : Double{
    val price = cryptoPrices[coinName] ?: 0.0
    return price
}

fun main(){
    val ethPrice = getPrice("ETH")
    println("Gia cua ETH la :$ethPrice")
    val dogePrice = getPrice("DOGE")
    println("Gia cua DOGE la : $dogePrice")
}
fun caculateDamage(distanceInfo : Any) : String{
    if (distanceInfo is Int){
        return when(distanceInfo) {
            in 0..15 -> ("Sat thuong toi da: 156HP")
            in 16..30 -> ("Sat thuong giam nhe: 150HP")
            else -> ("Sat thuong yeu: 124HP")
        }
    }
    else return ("Du lieu cu ly khong hop le!")
}

fun main(){
    println(caculateDamage(12))
    println(caculateDamage(25))
    println(caculateDamage(40))
    println(caculateDamage("Loi"))
}

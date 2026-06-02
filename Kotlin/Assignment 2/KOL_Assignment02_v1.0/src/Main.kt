import kotlin.math.abs

fun check(a: Int) = a%7==0 && a%5!=0

fun runAssignment1() {
    val resultList = mutableListOf<String>()
    for(i in 10..200){
        if(check(i)) resultList.add(i.toString())
    }
    println(resultList.joinToString(", "))
}

fun runAssignment2(){
    var n: Int? = null
    while (true){
        print("Input the number have 2 digits: ")
        val input = readlnOrNull()
        n = input?.toIntOrNull()

        if(n!= null && abs(n) in 10..99) break
        else println("Input invalid! Please input the number have 2 digits")
    }
    val binaryValue = n.toString(2)
    val hexValue = n.toString(16).uppercase()

    println("Your input number: $n")
    println("Perform at Binary: $binaryValue")
    println("Perform at Hex: $hexValue")
}



fun main(){
    runAssignment1()
    runAssignment2()
}
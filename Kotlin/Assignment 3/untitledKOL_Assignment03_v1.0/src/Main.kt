
fun runAssignment1(){
    val n = readlnOrNull()?.toIntOrNull() ?: 0
    val array = IntArray(n)
    for (i in 0 until n){
        array[i] = readlnOrNull()?.toIntOrNull() ?: 0;
    }
    array.sort()
    println("Sorted Array: ${array.joinToString(", ")}")
}

fun runAssignment2(){
    val s = readlnOrNull() ?: ""
    if(s.isBlank()){
        println("Empty string!")
        return
    }
    val wordCount = s.split(Regex("\\s+")).count{it.isNotBlank()}
    var isStartOfSentence = true
    val capitalizedString = s.map { char ->
        if (isStartOfSentence && char.isLetter()){
            isStartOfSentence = false
            char.uppercaseChar()
        } else{
            if(char=='.' || char == '!' || char == '?'){
                isStartOfSentence = true
            }
            char
        }
    }.joinToString("")
    println("Number of words in string: $wordCount")
    println("capitalized String: $capitalizedString")
}

fun runAssignment3(){
    val year = readlnOrNull()?.toIntOrNull() ?: 0
    val month = readlnOrNull()?.toIntOrNull() ?: 0
    val days = when(month){
        1,3,5,7,8,10,12 -> 31
        4,6,9,11 -> 30
        2->{
            val isLeapYear = (year % 400 == 0) || (year % 4==0 && year % 100 !=0)
            if(isLeapYear) 29 else 28
        }
        else -> {
            println("month invalid!")
            return
        }
    }
    println("In $month/$year has $days days")
}



fun main() {
    runAssignment1()
    runAssignment2()
    runAssignment3()
}
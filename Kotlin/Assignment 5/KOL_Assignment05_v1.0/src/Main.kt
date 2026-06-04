import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

suspend fun caculateFibonacci(n: Int) : Int = withContext(Dispatchers.Default){
    if(n <= 1) return@withContext n
    var a= 0
    var b = 1
    for (i in 2..n){
        val temp = a+b
        a= b
        b= temp
    }
    return@withContext b
}

fun runAssignment1() = runBlocking {
    println("\n--- ASSIGNMENT 1: FIBONACCI ---")
    print("Input a number: ")
    val n = readlnOrNull()?.toIntOrNull() ?: 0
    val result = caculateFibonacci(n)
    println("Fibonacci of n is: $result")
}

class InvalidNumberException(message : String = "Error Username has number!") : Exception(message)
class InvalidSpecialCharacterException (message: String = "Error Username has special character!") : Exception(message)
class InvalidLengthException (message: String = "Error Username's length smaller than 4!") : Exception(message)
class InvalidUsernameException (message: String = "Error Username not follow the rule(longer than 16 chars or uppercase Letter)") : Exception(message)

suspend fun validateUsername(username : String) : Boolean {
    return try {
        coroutineScope {
            if (username.length > 16 || username.any { it.isUpperCase() }) throw InvalidUsernameException()

            val job1 = async {
                if (username.any { it.isDigit() }) throw InvalidNumberException()
            }

            val job2 = async {
                val specialChars = listOf('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_')
                if (username.any() { it in specialChars }) throw InvalidSpecialCharacterException()
            }

            val job3 = async {
                if (username.length <= 4) throw InvalidLengthException()
            }

            awaitAll(job1, job2, job3)
            true
        }
    } catch (e: Exception) {
        println(e.message)
        false
    }
}

fun runAssignment2() = runBlocking {
    println("\n--- Assignment 2: VALIDATE USERNAME ---")
    val testCases = listOf("user123", "User", "us!er", "usr", "validname")
    for(username in testCases){
        println("Checking: $username")
        val isValid = validateUsername(username)
        println("=> ${if (isValid) "VALID!" else "INVALID"} ")
    }
}

fun sumToN(n: Int) : Int{
    var sum = 0
    for (i in 0..n) sum+=i
    return sum
}

fun sumFlow() : Flow<Int> = flow {
    for(i in 1..10){
        emit(sumToN(i))
        delay(500)
    }
}

fun runAssignment3() = runBlocking {
    println("\n--- Assignment 3: FLOW CACULATE SUM ---")
    val startTime = System.currentTimeMillis()

    sumFlow().collect{ result ->
        val timeElapse = System.currentTimeMillis() - startTime
        println("[+${timeElapse}ms collected sum: $result")
    }
}

fun main(){
    runAssignment1()
    runAssignment2()
    runAssignment3()
}
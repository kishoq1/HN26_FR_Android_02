import kotlinx.coroutines.*
import kotlin.random.Random


data class User(
    val name: String,
    val age: Int,
    val email: String
)

class UserManager{
    private val userList = mutableListOf<User>()

    suspend fun addUser(user : User){
        val delayTime = Random.nextLong(500,2001)
        println("Processing add user ${user.name}...(waiting for ${delayTime}ms)")
        delay(delayTime)
        userList.add(user)
        println("Added user successfully: ${user.name}")
    }

    fun displayUser(){
        if(userList.isEmpty()) println("List is empty!")
        else userList.forEach { println("-Name: ${it.name} | Age: ${it.age} | Email: ${it.email}") }
    }

    fun findUsersByName (searchName : String){
        println("Searching for: $searchName")
        val foundUsers = userList.filter { it.name.contains(searchName, ignoreCase = true) }

        if(foundUsers.isEmpty()) println("Not found any user!")
        else foundUsers.forEach { println("-Name: ${it.name} | Age: ${it.age} | Email: ${it.email}") }
    }

    fun sortUsersByName(){
        userList.sortBy { it.name }
        println("\n -> Sorted List by Name (from A-Z).")
    }
}

fun main() = runBlocking{
    val manager = UserManager()
    while(true){
        println("--- INPUT YOUR CHOICE ---")
        println("0. Exit program")
        println("1. Add User")
        println("2. Display list of Users")
        println("3. Sort users alphabetically")
        println("4. Find users by name")
        print("Your choice: ")
        when(readlnOrNull()?.toIntOrNull()){
            0 ->{
                println("Exited program!")
                break
            }
            1-> {
                print("Input name: ")
                val name = readlnOrNull() ?: ""
                print("Input age: ")
                val age = readlnOrNull()?.toIntOrNull() ?: 0
                print("Input Email: ")
                val email = readlnOrNull() ?: ""
                manager.addUser(User(name, age, email))
            }
            2 ->{
                println("\n --- LIST OF USERS ---")
                println("\n----------------------------------")
                manager.displayUser()
                println("----------------------------------\n")
            }
            3 ->{
                manager.sortUsersByName()
            }
            4 ->{
                print("Input user's name: ")
                val name = readlnOrNull() ?: ""
                manager.findUsersByName(name)
            }
            else -> println("Input Invalid! Please input a number from 0 to 4")
        }
    }
}

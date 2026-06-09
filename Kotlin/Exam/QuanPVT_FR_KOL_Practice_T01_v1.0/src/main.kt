import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class User(
    val name: String,
    val age: Int,
    val email: String
)

class UserManager{
    var userList = ArrayList<User>()

    suspend fun addUsers(user: User){
        val delayTime = Random.nextLong(500,2001)
        println("Adding user ${user.name} to database in $delayTime ms...")
        delay(delayTime)
        userList.add(user)
        println("Added user to database successfully!!!")
    }

    fun displayListOfUsers(){
        if (userList.isEmpty()) println("No user to display!!!")
        else userList.forEach { println("Name: ${it.name} | Age: ${it.age} | Email: ${it.email}") }
    }

    fun findUsersByName(searchUser : String){
        val foundUsers = userList.filter { it.name.contains(searchUser) }
        if (foundUsers.isEmpty()) println("Not found any user matching with $searchUser!!!")
        else foundUsers.forEach { println("Name: ${it.name} | Age: ${it.age} | Email: ${it.email}") }
    }

    fun sortUsersByName(){
        val sortedList = userList.sortedBy { it.name }
        userList = ArrayList(sortedList)
        println("Sorted users by name alphabetically!!!")
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun main() {
    val manager = UserManager()
    GlobalScope.launch {
        while (true) {
            println("\n--- USERS MANAGEMENT ---")
            println("0. Exit program")
            println("1. Add new User")
            println("2. Display all users")
            println("3. Find users by name")
            println("4. Sort User Alphabetically")
            print("Your choice:")
            when (readlnOrNull()?.toIntOrNull()) {
                0 -> {
                    println("Exited program!!!")
                    break
                }

                1 -> {
                    print("Input name: ")
                    val name = readlnOrNull() ?: ""
                    print("Input age: ")
                    val age = readlnOrNull()?.toIntOrNull() ?: 0
                    print("Input email: ")
                    val email = readlnOrNull() ?: ""
                    manager.addUsers(User(name, age, email))
                }

                2 -> {
                    println("--- LIST OF USERS ---")
                    println("-----------------------------------------")
                    manager.displayListOfUsers()
                    println("-----------------------------------------")
                }

                3 -> {
                    print("Input user's name: ")
                    val name = readlnOrNull() ?: ""
                    manager.findUsersByName(name)
                }

                4 -> {
                    manager.sortUsersByName()
                }

                else -> println("Invalid input!. Please input a number from 1 to 4.")
            }
        }
    }
    Thread.sleep(300000)
}
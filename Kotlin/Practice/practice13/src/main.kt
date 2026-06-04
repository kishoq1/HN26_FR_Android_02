import kotlinx.coroutines.*
import kotlin.random.Random
data class TacticalAgent(
    val agentName : String,
    val role : String,
    val combatScore: Int
)

class RosterManager{
    val agentList = mutableListOf<TacticalAgent>()

    suspend fun fetchAndAddAgent(agent: TacticalAgent){
        val delayTimes = Random.nextLong(800, 2500)
        println("Adding infor of ${agent.agentName} into server... ")
        delay(delayTimes)
        agentList.add(agent)
        println("Added agent ${agent.agentName} successfully")
    }

    fun displayAgentsList(){
        if(agentList.isEmpty()) println("No agent in server!!!")
        else
            agentList.forEach { println("Agent: ${it.agentName} | Role: ${it.role} | Combat Score: ${it.combatScore}") }
    }

    fun findAgentsByRole(searchRole : String){
        val foundAgents = agentList.filter { it.role.equals(searchRole, ignoreCase = true) }
        if(foundAgents.isEmpty()) println("Not found any agent with role: $searchRole")
        else foundAgents.forEach { println("Agent: ${it.agentName} | Combat Score: ${it.combatScore}") }
    }

    fun sortAgentsByScore(){
        agentList.sortByDescending { it.combatScore }
        println("Sorted by combat score! (from high to low)")
    }
}

fun main() = runBlocking {
    val manager = RosterManager()
    while(true){
        println("--- Choose your option ---")
        println("0. Exit program")
        println("1. Add an agent")
        println("2. display all agents in server")
        println("3. Find agents by role")
        println("4. Sort agents by combat score from high to low")
        print("Your choice: ")
        when(readlnOrNull()?.toIntOrNull()){
            0 -> {
                println("Exited program!")
                break
            }
            1 -> {
                print("Input agent's name: ")
                val name = readlnOrNull() ?: ""
                print("Input agent's role: ")
                val role = readlnOrNull() ?: ""
                print("Input agent's combat score: ")
                val score = readlnOrNull()?.toIntOrNull() ?: 0
                manager.fetchAndAddAgent(TacticalAgent(name, role, score))
            }
            2 -> {
                println("--- LIST OF AGENTS ---")
                println("-----------------------------------------------")
                manager.displayAgentsList()
                println("-----------------------------------------------")
            }
            3 -> {
                print("Input agent's role: ")
                val role = readlnOrNull() ?: ""
                manager.findAgentsByRole(role)
            }
            4 -> {
                manager.sortAgentsByScore()
            }
            else -> println("Invalid input. Please input a number from 0 to 4!")
        }
    }
}
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.KMutableProperty
data class TaticalAgent(
    val agentName : String,
    private var ultimatePoints : Int = 0,
    private var isFlanking : Boolean = false
){
    fun getPublicStatus(){
        println("Agent name is : ${this.agentName}")
    }

    private fun addUltimatePoint(point: Int){
        this.ultimatePoints += point
        println("Utimate points is ${this.ultimatePoints} points")
    }

    private fun executeFlank(){
        println("Agent ${this.agentName} is flanking the enemies!")
    }
}

fun main(){
    val omen = TaticalAgent("Omen");
    val kClass =TaticalAgent::class
    kClass.declaredMemberProperties.forEach { property ->
        property.isAccessible = true
        val value =property.get(omen)
        println("Property: ${property.name}, value: $value")
    }
    val addUltFunc = kClass.declaredMemberFunctions.find { it.name == "addUltimatePoint" }
    if(addUltFunc != null){
        addUltFunc.isAccessible = true
        addUltFunc.call(omen,3)
    }

    val flankingProp = kClass.declaredMemberFunctions.find { it.name == "executeFlank" }
    if (flankingProp is KMutableProperty<*>){
        flankingProp.isAccessible = true
        flankingProp.call(omen, true)
    }
    val executeFunc = kClass.declaredMemberFunctions.find { it.name == "executeFlank" }
    if(executeFunc != null){
        executeFunc.isAccessible = true
        executeFunc.call(omen)
    }
}
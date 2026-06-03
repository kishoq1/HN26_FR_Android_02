enum class ComponentType{
    CPU, VGA, RAM
}

data class PcComponent(
    val name : String,
    val type : ComponentType,
    var curTemp : Int,
    val safeTemp: Int
)

fun PcComponent.runStressTest(){
    this.curTemp += 20
    println("dang ep tai ${this.name}. Nhiet do tang len ${this.curTemp}")
}

sealed class TestResult{
    object Passed : TestResult()
    data class Overheated(val message: String) : TestResult()
}

fun evaluateComponent(component: PcComponent, onResult: (TestResult)  -> Unit){
    if (component.curTemp > component.safeTemp) onResult(TestResult.Overheated("Linh kiện ${component.name} đã vượt ngưỡng an toàn"))
    else onResult(TestResult.Passed)
}

fun main(){
    val component = PcComponent("Galax RTX 2060 super", ComponentType.VGA, 50, 85).also {
        println("Bat dau test linh kien ${it.name} (nguong an toan: ${it.safeTemp} do C)")
    }
    component.runStressTest()
    component.runStressTest()
    evaluateComponent(component){ result ->
        when(result){
            is TestResult.Passed -> println("Test Passed: linh kien hoat dong on dinh")
            is TestResult.Overheated -> println("Test Failed: ${result.message}")
        }

    }

}
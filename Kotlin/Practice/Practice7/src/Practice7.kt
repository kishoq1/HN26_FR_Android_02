data class SensorData(
    val component : String,
    val temp : Int,
    val fanSpeed : Int
)

fun main(){
    val list = listOf(
        SensorData("CPU Core i9", 85, 3000),
        SensorData("VGA RTX 4090", 65, 1500),
        SensorData("RAM DDR5", 45, 0)
    )
    for((component, temp, speed) in list){
        if(temp >= 80) println("$component dang hoat dong qua nhiet($temp do C), quat quay o muc $speed")
        else println("$component hoat dong binh thuong o $temp do C")
    }
}
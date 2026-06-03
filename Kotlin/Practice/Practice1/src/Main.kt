fun checkVgaTemperature (temp : Int) : String = when(temp){
        in Int.MIN_VALUE .. 49 -> ("VGA đang rat mat me")
        in 50..75 -> ("Nhiet do hoat dong binh thuong")
        in 76..85 -> ("VGA kha nong, can chu y")
        else -> ("Canh bao: qua nhiet")
}


fun main(){
    print("Nhap nhiet do VGA hien tai:")
    val currentTemp = readlnOrNull()?.toIntOrNull() ?: 0
    val statusMessage = checkVgaTemperature(currentTemp)
    println(statusMessage)
}
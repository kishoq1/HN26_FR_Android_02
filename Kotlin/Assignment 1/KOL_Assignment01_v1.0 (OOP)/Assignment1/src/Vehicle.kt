abstract class Vehicle(
    val licensePlate: String,
    val yearOfManufacture: Int,
    val basePrice: Double
){
    open fun displayInfo(){
        println("licensePlate: $licensePlate, year of manufacture: $yearOfManufacture, base price: $basePrice")
    }

    abstract fun caculateTotalPrice(): Double
}

class Car(
    licensePlate: String,
    yearOfManufacture: Int,
    basePrice: Double,
    val numberOfSeats: Int
) : Vehicle(licensePlate, yearOfManufacture, basePrice){
    override fun displayInfo() {
        print("[Car] number of seats: $numberOfSeats | " )
        super.displayInfo()
    }
    override fun caculateTotalPrice() : Double{
        return basePrice + (basePrice * 0.10)
    }
}

class Motocycle(
    licensePlate: String,
    yearOfManufacture: Int,
    basePrice: Double,
    val engineCapacity: Int
) : Vehicle(licensePlate, yearOfManufacture, basePrice){
    override fun displayInfo() {
        print("[Motocycle] engine Capacity: ${engineCapacity}cc | ")
        super.displayInfo()
    }

    override fun caculateTotalPrice(): Double {
        return basePrice + (basePrice * 0.05)
    }
}

class VehicleManager{
    private val vehicles = mutableListOf<Vehicle>()
    fun addVehicle(vehicle: Vehicle){
        vehicles.add(vehicle)
        println("Added vehicle have license plate: ${vehicle.licensePlate}")
    }

    fun displayList(){
        println("\n --- LIST OF VEHICLES ---")
        if (vehicles.isEmpty()){
            print("List is empty.")
            return
        }
        for (vehicle in vehicles){
            vehicle.displayInfo()
        }
    }
    fun caculateTotalInventoryPrice(): Double{
        var total = 0.0
        for(vehicle in vehicles){
            total += vehicle.caculateTotalPrice()
        }
        return total
    }
}

fun main(){
    val manager = VehicleManager()
    while(true){
        println("\n--- MENU OF VEHICLE MANAGER ---")
        println("1. Add a Car")
        println("2. Add a Motocycle")
        println("3. Display list of vehicles")
        println("4. Display total price")
        println("0. Exit program")
        print("Your choice: ")
        val choice = readlnOrNull()?.toIntOrNull() ?: -1
        when(choice){
            0 -> {
                println("Program closed. See you again!")
                break
            }
            1 -> {
                println("\n [Input info of the car]")
                print("Input the license plate: ")
                val plate = readlnOrNull() ?: ""
                print("Input year of manufacture: ")
                val year = readlnOrNull()?.toIntOrNull() ?: 0
                print("Input the base price: ")
                val price = readlnOrNull()?.toDoubleOrNull() ?: 0.0
                print("Input the number of seats: ")
                val seats = readlnOrNull()?.toIntOrNull() ?: 0

                val car = Car(plate, year, price, seats)
                manager.addVehicle(car)
            }
            2 -> {
                println("[Input info of the motocycle]")
                print("Input the license plate: ")
                val plate = readlnOrNull() ?: ""
                print("Input year of manufacture: ")
                val year = readlnOrNull()?.toIntOrNull() ?: 0
                print("Input the base price: ")
                val price = readlnOrNull()?.toDoubleOrNull() ?: 0.0
                print("input the engine capacity: ")
                val engine = readlnOrNull()?.toIntOrNull() ?: 0
                val moto = Motocycle(plate, year, price, engine)
                manager.addVehicle(moto)
            }
            3 ->{
                manager.displayList()
            }
            4 ->{
                val total = manager.caculateTotalInventoryPrice()
                println("=======================================")
                println("TOTAL PRICE OF ALL VEHICLES: $total")
                println("=======================================")
            }
            else -> {
                println("Choice not true. Plese input from 0 to 4!")
            }
        }
    }
}

enum class WeaponTier{
    SIDEARM, SMG, RIFLE, SNIPER
}

data class Weapon(
    val name : String,
    val type : WeaponTier,
    val maxAmmo : Int,
    var curAmmo : Int
)

fun Weapon.reload(){
    this.curAmmo = this.maxAmmo
    println("Da nap day bang dan cho ${this.name} voi ${this.maxAmmo} vien")
}

sealed class FireResult(){
    data class Success(val cur: String) : FireResult()
    object NeedReload : FireResult()
    data class Jammed (val message: String) : FireResult()
}

fun pullTrigger(weapon: Weapon, shotsFired: Int, onResult: (FireResult) -> Unit){
    if (weapon.curAmmo == 0) onResult(FireResult.NeedReload)
    else if(shotsFired > weapon.curAmmo){
        onResult(FireResult.Jammed("Co xa $shotsFired vien nhung sung chi con ${weapon.curAmmo} vien"))
    }
    else{
        weapon.curAmmo -= shotsFired
        onResult(FireResult.Success("sung con ${weapon.curAmmo} vien") )
    }
}

fun main(){
    val gun = Weapon("Vandal",WeaponTier.RIFLE, 25, 5).also {
        println("Da trang bi vu khi ${it.name} voi luong dan ${it.curAmmo}")
    }
    val n = readlnOrNull()?.toIntOrNull() ?: 0
    pullTrigger(gun, n){
        result -> when(result){
            is FireResult.Success -> println("Da xa thanh cong :${result.cur}")
        is FireResult.Jammed -> {
            println("Khong the xa dan: ${result.message}")
            gun.reload()
        }
        FireResult.NeedReload -> println("Dan da het, vui long nap them dan")
        }
    }
}
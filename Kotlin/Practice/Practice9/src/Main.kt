fun isSecurePort(port: Int) : Boolean{
    return port == 443 || port == 8443
}

fun main(){
    val ports = listOf(80, 443, 21, 22, 8443)
    val safePorts = ports.filter(::isSecurePort)
    println("$ports")
    println("$safePorts")
}
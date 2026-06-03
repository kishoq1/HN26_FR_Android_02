import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

class People(
    public val age: Int,
    private val gender: String
) {
    @JvmName("getAgeFunc")
    fun getAge(): Int {
        return age
    }
    private fun getGender(): String {
        return gender
    }
}

fun main() {
    println("--- BẮT ĐẦU KIỂM TRA REFLECTION ---")
    val person = People(age = 25, gender = "Nam")

    val kClass = People::class

    println("\n1. QUÉT VÀ GỌI CÁC THUỘC TÍNH (PROPERTIES):")
    kClass.declaredMemberProperties.forEach { property ->
        property.isAccessible = true

        val value = property.get(person)

        println("- Thuộc tính: [${property.name}] | Phạm vi: ${property.visibility} | Giá trị lấy được: $value")
    }

    println("\n2. QUÉT VÀ GỌI CÁC HÀM (FUNCTIONS):")
    kClass.declaredMemberFunctions.forEach { function ->
        function.isAccessible = true
        val result = function.call(person)

        println("- Hàm: [${function.name}()] | Phạm vi: ${function.visibility} | Kết quả thực thi: $result")
    }
}
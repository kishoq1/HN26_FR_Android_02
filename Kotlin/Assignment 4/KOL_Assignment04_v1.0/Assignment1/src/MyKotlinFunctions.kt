class CustomLong(val value: Long){
    operator fun plus(other: CustomLong) = CustomLong(this.value + other.value)
    operator fun minus(other: CustomLong) = CustomLong(this.value - other.value)
    operator fun times(other: CustomLong) = CustomLong(this.value * other.value)
    operator fun div(other: CustomLong) = CustomLong(this.value / other.value)
}
package ua.kpi.its.lab.security.dto

data class MobilePhoneResponseDto(
    val id: Long,
    val brand: String,
    val manufacturer: String,
    val model: String,
    val memorySize: Double,
    val price: Double,
    val releaseDate: String,
    val dualSimSupport: Boolean,
)

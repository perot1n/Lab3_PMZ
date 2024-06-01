package ua.kpi.its.lab.security.dto

data class FileResponseDto(
    val id: Long,
    val name: String,
    val extension: String,
    val size: Double,
    val creationDate: String,
    val photo: Boolean,
    val mobilePhoneId: Long
)

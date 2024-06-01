package ua.kpi.its.lab.security.dto

import kotlinx.serialization.Serializable

@Serializable

data class FileResponseDto(
    val id: Long,
    val name: String,
    val extension: String,
    val size: Double,
    val creationDate: String,
    val photo: Boolean,
    val mobilePhoneId: Long
)

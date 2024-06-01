package ua.kpi.its.lab.security.entity

import jakarta.persistence.*

@Entity
data class File(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val extension: String,
    val size: Double,
    val creationDate: String,
    val photo: Boolean,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mobile_phone_id")
    val mobilePhone: MobilePhone
)

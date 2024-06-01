package ua.kpi.its.lab.security.entity

import jakarta.persistence.*

@Entity
data class MobilePhone(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val brand: String,
    val manufacturer: String,
    val model: String,
    val memorySize: Double,
    val price: Double,
    val releaseDate: String,
    val dualSimSupport: Boolean,
    @OneToMany(mappedBy = "mobilePhone", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val files: List<File> = emptyList()
) : Comparable<MobilePhone> {
    override fun compareTo(other: MobilePhone): Int {
        val nameComparison = brand.compareTo(other.brand)
        return if (nameComparison != 0) {
            nameComparison
        } else {
            id.compareTo(other.id)
        }

    }
}

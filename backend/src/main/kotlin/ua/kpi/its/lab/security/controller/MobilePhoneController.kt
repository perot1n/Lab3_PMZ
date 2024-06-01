package ua.kpi.its.lab.security.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ua.kpi.its.lab.security.dto.*
import ua.kpi.its.lab.security.svc.MobilePhoneService
import ua.kpi.its.lab.security.entity.MobilePhone

@RestController
@RequestMapping("/api/phones")
class MobilePhoneController(private val phoneService: MobilePhoneService) {

    @PostMapping
    fun createPhone(@RequestBody requestDto: MobilePhoneRequestDto): ResponseEntity<MobilePhoneResponseDto> {
        val phone = phoneService.create(
            MobilePhone(
                brand = requestDto.brand,
                manufacturer = requestDto.manufacturer,
                model = requestDto.model,
                memorySize = requestDto.memorySize,
                price = requestDto.price,
                releaseDate = requestDto.releaseDate,
                dualSimSupport = requestDto.dualSimSupport
            )
        )
        val responseDto = MobilePhoneResponseDto(
            id = phone.id,
            brand = phone.brand,
            manufacturer = phone.manufacturer,
            model = phone.model,
            memorySize = phone.memorySize,
            price = phone.price,
            releaseDate = phone.releaseDate,
            dualSimSupport = phone.dualSimSupport
        )
        return ResponseEntity.ok(responseDto)
    }

    @GetMapping("/{id}")
    fun getPhoneById(@PathVariable id: Long): ResponseEntity<MobilePhoneResponseDto> {
        val phone = phoneService.getById(id)
        return if (phone != null) {
            val responseDto = MobilePhoneResponseDto(
                id = phone.id,
                brand = phone.brand,
                manufacturer = phone.manufacturer,
                model = phone.model,
                memorySize = phone.memorySize,
                price = phone.price,
                releaseDate = phone.releaseDate,
                dualSimSupport = phone.dualSimSupport
            )
            ResponseEntity.ok(responseDto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{id}")
    fun updatePhone(@PathVariable id: Long, @RequestBody requestDto: MobilePhoneRequestDto): ResponseEntity<MobilePhoneResponseDto> {
        val phone = phoneService.update(
            MobilePhone(
                id = id,
                brand = requestDto.brand,
                manufacturer = requestDto.manufacturer,
                model = requestDto.model,
                memorySize = requestDto.memorySize,
                price = requestDto.price,
                releaseDate = requestDto.releaseDate,
                dualSimSupport = requestDto.dualSimSupport
            )
        )
        val responseDto = MobilePhoneResponseDto(
            id = phone.id,
            brand = phone.brand,
            manufacturer = phone.manufacturer,
            model = phone.model,
            memorySize = phone.memorySize,
            price = phone.price,
            releaseDate = phone.releaseDate,
            dualSimSupport = phone.dualSimSupport
        )
        return ResponseEntity.ok(responseDto)
    }

    @DeleteMapping("/{id}")
    fun deletePhone(@PathVariable id: Long): ResponseEntity<Void> {
        phoneService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getAllPhones(): ResponseEntity<List<MobilePhoneResponseDto>> {
        val phones = phoneService.getAll().map { phone ->
            MobilePhoneResponseDto(
                id = phone.id,
                brand = phone.brand,
                manufacturer = phone.manufacturer,
                model = phone.model,
                memorySize = phone.memorySize,
                price = phone.price,
                releaseDate = phone.releaseDate,
                dualSimSupport = phone.dualSimSupport
            )
        }
        return ResponseEntity.ok(phones)
    }
}

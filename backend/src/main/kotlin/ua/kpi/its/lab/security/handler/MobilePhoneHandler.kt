package ua.kpi.its.lab.security.handler

import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import ua.kpi.its.lab.security.dto.*
import ua.kpi.its.lab.security.svc.MobilePhoneService
import ua.kpi.its.lab.security.entity.MobilePhone

@Component
class MobilePhoneHandler(private val phoneService: MobilePhoneService) {

    fun createPhoneHandler(request: ServerRequest): ServerResponse {
        val requestDto = request.body(MobilePhoneRequestDto::class.java)
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
        return ServerResponse.ok().body(responseDto)
    }

    fun getPhoneByIdHandler(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
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
            ServerResponse.ok().body(responseDto)
        } else {
            ServerResponse.notFound().build()
        }
    }

    fun updatePhoneHandler(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val requestDto = request.body(MobilePhoneRequestDto::class.java)
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
        return ServerResponse.ok().body(responseDto)
    }

    fun deletePhoneHandler(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        phoneService.deleteById(id)
        return ServerResponse.noContent().build()
    }

    fun getAllPhonesHandler(request: ServerRequest): ServerResponse {
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
        return ServerResponse.ok().body(phones)
    }
}

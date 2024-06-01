package ua.kpi.its.lab.security.handler

import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import ua.kpi.its.lab.security.dto.*
import ua.kpi.its.lab.security.svc.FileService
import ua.kpi.its.lab.security.svc.MobilePhoneService
import ua.kpi.its.lab.security.entity.File

@Component
class FileHandler(
    private val fileService: FileService,
    private val phoneService: MobilePhoneService
) {

    fun createFileHandler(request: ServerRequest): ServerResponse {
        val requestDto = request.body(FileRequestDto::class.java)
        val phone = phoneService.getById(requestDto.mobilePhoneId)
        return if (phone != null) {
            val file = fileService.create(
                File(
                    name = requestDto.name,
                    extension = requestDto.extension,
                    size = requestDto.size,
                    creationDate = requestDto.creationDate,
                    photo = requestDto.photo,
                    mobilePhone = phone
                )
            )
            val responseDto = FileResponseDto(
                id = file.id,
                name = file.name,
                extension = file.extension,
                size = file.size,
                creationDate = file.creationDate,
                photo = file.photo,
                mobilePhoneId = file.mobilePhone.id
            )
            ServerResponse.ok().body(responseDto)
        } else {
            ServerResponse.badRequest().build()
        }
    }

    fun getFileByIdHandler(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val file = fileService.getById(id)
        return if (file != null) {
            val responseDto = FileResponseDto(
                id = file.id,
                name = file.name,
                extension = file.extension,
                size = file.size,
                creationDate = file.creationDate,
                photo = file.photo,
                mobilePhoneId = file.mobilePhone.id
            )
            ServerResponse.ok().body(responseDto)
        } else {
            ServerResponse.notFound().build()
        }
    }

    fun updateFileHandler(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val requestDto = request.body(FileRequestDto::class.java)
        val phone = phoneService.getById(requestDto.mobilePhoneId)
        return if (phone != null) {
            val file = fileService.update(
                File(
                    id = id,
                    name = requestDto.name,
                    extension = requestDto.extension,
                    size = requestDto.size,
                    creationDate = requestDto.creationDate,
                    photo = requestDto.photo,
                    mobilePhone = phone
                )
            )
            val responseDto = FileResponseDto(
                id = file.id,
                name = file.name,
                extension = file.extension,
                size = file.size,
                creationDate = file.creationDate,
                photo = file.photo,
                mobilePhoneId = file.mobilePhone.id
            )
            ServerResponse.ok().body(responseDto)
        } else {
            ServerResponse.badRequest().build()
        }
    }

    fun deleteFileHandler(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        fileService.deleteById(id)
        return ServerResponse.noContent().build()
    }

    fun getAllFilesHandler(request: ServerRequest): ServerResponse {
        val files = fileService.getAll().map { file ->
            FileResponseDto(
                id = file.id,
                name = file.name,
                extension = file.extension,
                size = file.size,
                creationDate = file.creationDate,
                photo = file.photo,
                mobilePhoneId = file.mobilePhone.id
            )
        }
        return ServerResponse.ok().body(files)
    }
}

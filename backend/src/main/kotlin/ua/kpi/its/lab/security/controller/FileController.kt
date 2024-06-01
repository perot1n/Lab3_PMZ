package ua.kpi.its.lab.security.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ua.kpi.its.lab.security.dto.*
import ua.kpi.its.lab.security.svc.FileService
import ua.kpi.its.lab.security.svc.MobilePhoneService
import ua.kpi.its.lab.security.entity.File

@RestController
@RequestMapping("/api/files")
class FileController(
    private val fileService: FileService,
    private val phoneService: MobilePhoneService
) {

    @PostMapping
    fun createFile(@RequestBody requestDto: FileRequestDto): ResponseEntity<FileResponseDto> {
        val phone = phoneService.getById(requestDto.mobilePhoneId)
        if (phone != null) {
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
            return ResponseEntity.ok(responseDto)
        } else {
            return ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("/{id}")
    fun getFileById(@PathVariable id: Long): ResponseEntity<FileResponseDto> {
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
            ResponseEntity.ok(responseDto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{id}")
    fun updateFile(@PathVariable id: Long, @RequestBody requestDto: FileRequestDto): ResponseEntity<FileResponseDto> {
        val phone = phoneService.getById(requestDto.mobilePhoneId)
        if (phone != null) {
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
            return ResponseEntity.ok(responseDto)
        } else {
            return ResponseEntity.badRequest().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteFile(@PathVariable id: Long): ResponseEntity<Void> {
        fileService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getAllFiles(): ResponseEntity<List<FileResponseDto>> {
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
        return ResponseEntity.ok(files)
    }
}

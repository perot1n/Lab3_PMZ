package ua.kpi.its.lab.security.svc.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ua.kpi.its.lab.security.entity.File
import ua.kpi.its.lab.security.repo.FileRepository
import ua.kpi.its.lab.security.svc.FileService

@Service
class FileServiceImpl @Autowired constructor(
    private val fileRepository: FileRepository
) : FileService {
    override fun create(file: File): File {
        return fileRepository.save(file)
    }

    override fun getById(id: Long): File? {
        return fileRepository.findById(id).orElse(null)
    }

    override fun update(file: File): File {
        return fileRepository.save(file)
    }

    override fun deleteById(id: Long) {
        fileRepository.deleteById(id)
    }

    override fun getAll(): List<File> {
        return fileRepository.findAll()
    }
}

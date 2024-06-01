package ua.kpi.its.lab.security.svc.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ua.kpi.its.lab.security.entity.MobilePhone
import ua.kpi.its.lab.security.repo.MobilePhoneRepository
import ua.kpi.its.lab.security.svc.MobilePhoneService

@Service
class MobilePhoneServiceImpl @Autowired constructor(
    private val phoneRepository: MobilePhoneRepository
) : MobilePhoneService {
    override fun create(phone: MobilePhone): MobilePhone {
        return phoneRepository.save(phone)
    }

    override fun getById(id: Long): MobilePhone? {
        return phoneRepository.findById(id).orElse(null)
    }

    override fun update(phone: MobilePhone): MobilePhone {
        return phoneRepository.save(phone)
    }

    override fun deleteById(id: Long) {
        phoneRepository.deleteById(id)
    }

    override fun getAll(): List<MobilePhone> {
        return phoneRepository.findAll()
    }
}

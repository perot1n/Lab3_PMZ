package ua.kpi.its.lab.security.svc

import ua.kpi.its.lab.security.entity.MobilePhone

interface MobilePhoneService {
    fun create(phone: MobilePhone): MobilePhone
    fun getById(id: Long): MobilePhone?
    fun update(phone: MobilePhone): MobilePhone
    fun deleteById(id: Long)
    fun getAll(): List<MobilePhone>
}

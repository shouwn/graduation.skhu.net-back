package com.shouwn.graduation.model.domain.type

import org.neo4j.ogm.typeconversion.AttributeConverter
import java.io.Serializable

enum class RoleName constructor(
        val value: String
) : Serializable{
    ROLE_STUDENT("STUDENT"), // 회원 가입한 유저
    ROLE_ADMIN("ADMIN")
    ;

    companion object {
        private val map = RoleName.values().associate { it.value to it }

        fun valueOfRole(value: String): RoleName =
                map[value] ?: throw IllegalStateException("$value 에 해당하는 타입이 없습니다.")
    }
}

class RoleTypeConverter : AttributeConverter<RoleName, String> {
    override fun toGraphProperty(value: RoleName?): String =
            value?.value ?: throw IllegalStateException()

    override fun toEntityAttribute(value: String?): RoleName =
            value?.let { RoleName.valueOfRole(it) } ?: throw IllegalStateException()
}
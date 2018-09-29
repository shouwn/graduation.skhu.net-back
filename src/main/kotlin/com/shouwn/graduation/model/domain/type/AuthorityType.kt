package com.shouwn.graduation.model.domain.type

import org.neo4j.ogm.typeconversion.AttributeConverter
import java.io.Serializable

enum class AuthorityType constructor(
        val value: Int,
        val role: String
) : Serializable{
    USER(0, AuthorityType.ROLE_USER), // 회원 가입한 유저
    GUEST(1, AuthorityType.ROLE_GUEST), // 회원 가입하지 않은 게스트
    ;

    companion object {
        const val ROLE_USER = "ROLE_USER"
        const val ROLE_GUEST = "ROLE_GUEST"

        val map = AuthorityType.values().associate { it.value to it }

        fun valueOf(value: Int): AuthorityType =
                map[value] ?: throw IllegalStateException("$value 에 해당하는 타입이 없습니다.")
    }
}

class AuthorityTypeConverter : AttributeConverter<AuthorityType, Number> {
    override fun toGraphProperty(value: AuthorityType?): Number =
            value?.value ?: throw IllegalStateException()

    override fun toEntityAttribute(value: Number?): AuthorityType =
            value?.let { AuthorityType.valueOf(it.toInt()) } ?: throw IllegalStateException()
}
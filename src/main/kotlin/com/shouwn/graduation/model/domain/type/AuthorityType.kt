package com.shouwn.graduation.model.domain.type

import org.neo4j.ogm.typeconversion.AttributeConverter

enum class AuthorityType constructor(
        val value: Int
){
    ROLE_USER(0), // 회원 가입한 유저
    ROLE_GUEST(1), // 회원 가입하지 않은 게스트
    ;

    companion object {
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
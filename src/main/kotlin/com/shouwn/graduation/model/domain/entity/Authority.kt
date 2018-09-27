package com.shouwn.graduation.model.domain.entity

import com.shouwn.graduation.model.domain.type.AuthorityType
import com.shouwn.graduation.model.domain.type.AuthorityTypeConverter
import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.typeconversion.Convert
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class Authority constructor(
        @Id @GeneratedValue
        var id: Long? = null,

        @Property
        @Convert(AuthorityTypeConverter::class)
        val authority: AuthorityType
)
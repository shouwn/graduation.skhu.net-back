package com.shouwn.graduation.model.domain.entity

import com.shouwn.graduation.model.domain.type.*
import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.typeconversion.Convert
import java.io.Serializable

@NodeEntity(label = "Role")
data class Role constructor(
        @Id @GeneratedValue
        var id: Long? = null,

        @Property(name = "role")
        @Convert(RoleTypeConverter::class)
        var role: RoleName

) : Serializable
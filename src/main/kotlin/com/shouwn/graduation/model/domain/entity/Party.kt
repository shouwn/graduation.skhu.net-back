package com.shouwn.graduation.model.domain.entity

import org.neo4j.ogm.annotation.*

@NodeEntity
data class Party constructor(

        @Id @GeneratedValue
        var id: Long? = null,

        @Index(unique = true)
        var name: String,

        @Relationship(type = "SUB")
        var subParties: List<Party>
)
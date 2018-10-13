package com.shouwn.graduation.model.domain.entity

import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity

@NodeEntity
data class Party constructor(

        @Id @GeneratedValue
        var id: Long? = null,

        var name: String
)
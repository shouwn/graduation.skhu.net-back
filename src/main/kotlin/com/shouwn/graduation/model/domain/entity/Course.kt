package com.shouwn.graduation.model.domain.entity

import com.shouwn.graduation.model.domain.entity.audit.UserDateAudit
import com.shouwn.graduation.model.domain.type.SectionType
import com.shouwn.graduation.model.domain.type.TermType
import org.apache.tomcat.jni.Local
import org.neo4j.ogm.annotation.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import java.io.Serializable
import java.time.LocalDateTime

@NodeEntity
data class Course constructor(

        @Id @GeneratedValue
        var id: Long? = null,

        @Index(unique = true)
        var code: String,

        @Index
        var name: String,

        var credit: Double,

        var enabled: Boolean,

        @Relationship(type = "REPLACE", direction = Relationship.OUTGOING)
        var replacement: Course? = null,

        @Relationship(type = "OPEN", direction = Relationship.INCOMING)
        var party: Party,

        var createdBy: Long,

        var updatedBy: Long,

        var createdAt: LocalDateTime = LocalDateTime.now(),

        var updatedAt: LocalDateTime = LocalDateTime.now()

) : Serializable {
        data class StorageValue constructor(
                var code: String,

                var name: String,

                var credit: Double,

                var enabled: Boolean,

                var partyName: String,

                var createdBy: Long,

                var updatedBy: Long,

                var createdAt: String = LocalDateTime.now().toString(),

                var updatedAt: String
        )
}
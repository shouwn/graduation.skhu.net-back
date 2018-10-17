package com.shouwn.graduation.repository

import com.shouwn.graduation.model.domain.dto.request.AttendRequest
import com.shouwn.graduation.model.domain.entity.Attend
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.query.Param

interface AttendRepository : Neo4jRepository<Attend, Long> {

    @Query("""
        WITH {attends} AS attends
        UNWIND attends AS attend
        MATCH (u: User { userNumber: attend.userNumber }), (c: Course { code: attend.courseCode })
        MERGE (u) -[a: ATTEND]-> (c)
        ON CREATE SET a.year = attend.year, a.term = attend.term.value, a.grade = attend.grade.value,
          a.section = attend.section.value, a.createdAt = attend.createdAt,
          a.updatedAt = attend.updatedAt,
          a.createdBy = attend.createdBy, a.updatedBy = attend.updatedBy
        ON MATCH SET a.year = attend.year, a.term = attend.term, a.grade = attend.grade,
          a.section = attend.section, a.updatedBy = attend.updatedBy, a.updatedAt = attend.updatedAt
    """)
    fun mergeAttend(@Param("attends") attends: List<Attend.StorageValue>)

    @Query("""
        WITH {attend} AS attend
        MATCH 
    """)
    fun updateAttend(@Param("attend") attend: AttendRequest)
}
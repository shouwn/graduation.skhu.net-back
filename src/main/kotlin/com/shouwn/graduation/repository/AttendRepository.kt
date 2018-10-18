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
        MATCH (u: User)
        WHERE id(u) = {userId}
        MATCH (c: Course { code: attend.courseCode })
        MERGE (u) -[a: ATTEND { year: attend.year, term: attend.term }]-> (c)
        ON CREATE SET a += attend, a.courseCode = null
        ON MATCH SET a.year = attend.year, a.term = attend.term, a.grade = attend.grade,
          a.section = attend.section, a.updatedBy = attend.updatedBy, a.updatedAt = attend.updatedAt
    """)
    fun mergeAttend(@Param("userId") userId: Long,
                    @Param("attends") attends: List<Attend.StorageDto>)

    @Query("""
        WITH {attend} AS attend
        MATCH (u: User)
        MATCH (c: Course)
        MATCH () -[old:ATTEND]-> ()
        WHERE id(u) = {userId} AND c.code = attend.courseCode AND id(old) = {attendId}
        CREATE p = (u) -[a:ATTEND]-> (c)
        SET a = old, a.year = attend.year, a.term = attend.term, a.grade = attend.grade,
          a.section = attend.section, a.updatedBy = attend.updatedBy, a.updatedAt = attend.updatedAt
        DELETE old
        RETURN p
    """)
    fun updateAttend(@Param("userId") userId: Long,
                     @Param("attendId") attendId: Long,
                     @Param("attend") attend: Attend.StorageDto): Attend
}
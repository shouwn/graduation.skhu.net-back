package com.shouwn.graduation.repository

import com.shouwn.graduation.model.domain.dto.request.AttendRequest
import com.shouwn.graduation.model.domain.entity.Attend
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.query.Param

interface AttendRepository : Neo4jRepository<Attend, Long> {

//    ON MATCH SET a.year = attend.year, a.term = attend.term.value, a.grade = attend.grade.value,
//    a.section = attend.section.value, a.updatedBy = attend.updatedBy, a.updatedAt = attend.updatedAt

    @Query("""
        OPTIONAL MATCH (u) -[r: ATTEND]-> ()
        WHERE id(u) = {userId}
        DELETE r WITH {attends} AS attends
        UNWIND attends AS attend
        MATCH (u: User)
        WHERE id(u) = {userId}
        MATCH (c: Course { code: attend.course.code })
        CREATE p = (u) -[a: ATTEND]-> (c)
        SET a.year = attend.year, a.term = attend.term.value, a.grade = attend.grade.value,
          a.section = attend.section.value, a.createdAt = attend.createdAt, a.createdBy = attend.createdBy,
          a.updatedBy = attend.updatedBy, a.updatedAt = attend.updatedAt WITH *
        RETURN p
    """)
    fun mergeAttend(@Param("userId") userId: Long,
                    @Param("attends") attends: List<Attend>): List<Attend>

    @Query("""
        WITH {attend} AS attend
        MATCH (u: User)
        MATCH (c: Course)
        MATCH () -[old:ATTEND]-> ()
        WHERE id(u) = {userId} AND c.code = attend.course.code AND id(old) = {attendId}
        CREATE p = (u) -[a:ATTEND]-> (c)
        SET a = old, a.year = attend.year, a.term = attend.term.value, a.grade = attend.grade.value,
          a.section = attend.section.value, a.updatedBy = attend.updatedBy, a.updatedAt = attend.updatedAt,
          a.createdAt = old.createdAt, a.createdBy = old.createdBy WITH *
        DELETE old WITH *
        RETURN p
    """)
    fun updateAttend(@Param("userId") userId: Long,
                     @Param("attendId") attendId: Long,
                     @Param("attend") attend: Attend): Attend
}
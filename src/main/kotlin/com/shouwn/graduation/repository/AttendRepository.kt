package com.shouwn.graduation.repository

import com.shouwn.graduation.model.domain.dto.request.AttendRequest
import com.shouwn.graduation.model.domain.entity.Attend
import com.shouwn.graduation.model.domain.type.AttendType
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.query.Param

interface AttendRepository : Neo4jRepository<Attend, Long> {

//    ON MATCH SET a.year = attend.year, a.term = attend.term.value, a.grade = attend.grade.value,
//    a.section = attend.section.value, a.updatedBy = attend.updatedBy, a.updatedAt = attend.updatedAt

    @Query("""
        MATCH p = () <-[attend:ATTEND]- (user: User)
        WHERE ID(user) = {userId}
        RETURN p ORDER BY attend.year, attend.term
    """)
    fun findAllByUserIdOrderByYearAndTerm(@Param("userId") userId: Long): List<Attend>

    @Query("""

    """)
    fun deleteByUserIdAndType(@Param("userId") userId: Long,
                              @Param("typeValue") typeValue: Long)

    @Query("""
        WITH {attends} AS attends
        UNWIND attends AS attend
        MATCH (u: User)
        WHERE id(u) = {userId}
        MATCH (c: Course { code: attend.course.code })
        CREATE p = (u) -[a: ATTEND]-> (c)
        SET a.year = attend.year, a.term = attend.term.value, a.grade = attend.grade.value,
          a.type = attend.type.value, a.name = attend.name, a.credit = attend.credit,
          a.section = attend.section.value, a.createdAt = attend.createdAt, a.createdBy = attend.createdBy,
          a.updatedBy = attend.updatedBy, a.updatedAt = attend.updatedAt WITH *
        RETURN p
    """)
    fun addAttend(@Param("userId") userId: Long,
                    @Param("attends") attends: List<Attend>): List<Attend>

    @Query("""
        MATCH (u) -[old:ATTEND]-> (c)
        WHERE id(old) = {attendId}
        MATCH (newCourse: Course)
        WHERE id(newCourse) = {courseId}
        CREATE p = (u) -[a:ATTEND]-> (newCourse)
        SET a = old, a.year = {year}, a.term = {term}, a.grade = {grade}, a.type = {type},
          a.section = {section}, a.updatedBy = {updatedBy}, a.updatedAt = {updatedAt},
          a.credit = {credit}, a.name = {name},
          a.createdAt = old.createdAt, a.createdBy = old.createdBy WITH *
        DELETE old WITH *
        RETURN p
    """)
    fun updateAttend(@Param("attendId") attendId: Long,
                     @Param("courseId") courseId: Long,
                     @Param("year") year: Long,
                     @Param("name") name: String,
                     @Param("credit") credit: Double,
                     @Param("term") term: Long,
                     @Param("grade") grade: Long,
                     @Param("section") section: Long,
                     @Param("type") type: Long,
                     @Param("updatedBy") updatedBy: Long,
                     @Param("updatedAt") updatedAt: String): Attend

    @Query("""
        WITH {type} AS attendType
        MATCH (u) -[r:ATTEND]-> ()
        WHERE ID(u) = {userId} AND r.type = attendType.value
        DELETE r
    """)
    fun deleteAllByUserIdAndType(@Param("userId") userId: Long,
                                 @Param("type") type: AttendType)
}
package com.shouwn.graduation.repository

import com.shouwn.graduation.model.domain.entity.Course
import com.shouwn.graduation.model.domain.entity.Requirement
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.query.Param

interface CourseRepository : Neo4jRepository<Course, Long>{

    @Query("""
        WITH {courses} AS courses
        UNWIND courses AS course
        MERGE (c: Course { code: course.code })
        ON CREATE SET c.code = course.code, c.name = course.name,
          c.enabled = course.enabled, c.createdBy = course.createdBy, c.createdAt = course.createdAt,
          c.updatedBy = course.updatedBy, c.updatedAt = course.updatedAt
        ON MATCH SET c.name = course.name, c.credit = course.credit, c.updatedBy = course.updatedBy, c.updatedAt = course.updatedAt WITH *
        MATCH (p: Party)
        WHERE id(p) = HEAD(course.parties).id
        MERGE path = (c) <-[:OPEN]- (p)
        RETURN path
    """)
    fun mergeCourse(@Param("courses") courses: List<Course>): List<Course>

    @Query(value = """
        WITH {partyIds} AS partyIds
        UNWIND partyIds AS partyId
        MATCH (c: Course)
        WHERE id(c) = {courseId}
        SET c.name = {courseName}, c.enabled = {courseEnabled},
          c.updatedBy = {updatedBy}, c.updatedAt = {updatedAt} WITH *
        MATCH (c) <-[r:OPEN]- ()
        DELETE r WITH *
        MATCH (p: Party)
        WHERE id(p) = partyId
        MERGE path = (c) <-[:OPEN]- (p) WITH *
        RETURN path
    """)
    fun update(@Param("partyIds") partyIds: Set<Long>,
               @Param("courseId") courseId: Long,
               @Param("courseName") courseName: String,
               @Param("courseEnabled") courseEnabled: Boolean,
               @Param("updatedBy") updatedBy: Long,
               @Param("updatedAt") updatedAt: String): Course

    @Query("""
        MATCH (c: Course)
        WHERE id(c) = {courseId}
        SET c.name = {courseName}, c.enabled = {courseEnabled},
          c.updatedBy = {updatedBy}, c.updatedAt = {updatedAt} WITH *
        RETURN (c) <-[:OPEN]- ()
    """)
    fun update(@Param("courseId") courseId: Long,
               @Param("courseName") courseName: String,
               @Param("courseEnabled") courseEnabled: Boolean,
               @Param("updatedBy") updatedBy: Long,
               @Param("updatedAt") updatedAt: String): Course

    @Query("""
        MATCH p = (c: Course) <-[:OPEN]- (party: Party)
        WHERE c.name =~ {name} AND c.code =~ {code} AND party.name =~ {partyName}
        RETURN p
    """)
    fun findAllLikeCodeAndName(@Param("code") code: String,
                               @Param("name") name: String,
                               @Param("partyName") partyName: String): List<Course>
}
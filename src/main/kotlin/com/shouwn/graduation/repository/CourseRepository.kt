package com.shouwn.graduation.repository

import com.shouwn.graduation.model.domain.entity.Course
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.query.Param

interface CourseRepository : Neo4jRepository<Course, Long>{

    @Query("""
        WITH {courses} AS courses
        UNWIND courses AS course
        MERGE (c: Course { code: course.code })
        ON CREATE SET c.code = course.code, c.name = course.name, c.credit = course.credit,
          c.enabled = course.enabled, c.createdBy = course.createdBy, c.createdAt = course.createdAt,
          c.updatedBy = course.updatedBy, c.updatedAt = course.updatedAt
        ON MATCH SET c.name = course.name, c.updatedBy = course.updatedBy, c.updatedAt = course.updatedAt WITH *
        MATCH (p: Party { name: course.party.name })
        MERGE path = (c) <-[:OPEN]- (p)
        RETURN path
    """)
    fun mergeCourse(@Param("courses") courses: List<Course>): List<Course>
}
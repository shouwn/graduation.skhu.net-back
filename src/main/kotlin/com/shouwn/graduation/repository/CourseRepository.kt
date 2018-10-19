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
        ON CREATE SET c += course
        ON MATCH SET c.name = course.name, c.updatedBy = course.updatedBy, c.updatedAt = course.updatedAt
        MERGE (p: Party { name: course.partyName })
        MERGE path = (c) <-[:OPEN]- (p)
        RETURN path
    """)
    fun mergeCourse(@Param("courses") courses: List<Course.StorageDto>): List<Course>
}
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
        ON CREATE SET c.name = course.name, c.section = course.section, c.credit = course.credit,
          c.term = course.term, c.enabled = course.enabled
        ON MATCH SET c.name = course.name
        MERGE (p: Party { name: course.party.name })
        MERGE (c) <-[:OPEN]- (p)
    """)
    fun mergeCourse(@Param("courses") courses: List<Course>)
}
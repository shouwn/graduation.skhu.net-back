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
          c.enabled = course.enabled, c.createdAt = course.createdAt, c.updatedAt = course.updatedAt,
          c.createdBy = course.createdBy, c.updatedBy = course.updatedBy
        ON MATCH SET c.name = course.name, c.updatedBy = course.updatedBy, c.updatedAt = course.updatedAt
        MERGE (p: Party { name: course.party })
        MERGE (c) <-[:OPEN]- (p)
    """)
    fun mergeCourse(@Param("courses") courses: List<Course.StorageDto>)
}
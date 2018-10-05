package com.shouwn.graduation.repository

import com.shouwn.graduation.model.domain.entity.Interview
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.query.Param

interface InterviewRepository : Neo4jRepository<Interview, Long> {

    @Query("""
        MATCH p = (a) -[:ASK]-> (i: Interview) <-[:WRITE]- (b)
        WHERE id(a) = {askerId}
        RETURN p
    """)
    fun findAllByAskerId(@Param("askerId") askerId: Long): List<Interview>

    @Query("""
        MATCH p = (a) -[:ASK]-> (i: Interview) <-[:WRITE]- (b)
        WHERE id(b) = {writerId}
        RETURN p
    """)
    fun findAllByWriterId(@Param("writerId") writerId: Long): List<Interview>
}
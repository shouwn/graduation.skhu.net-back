package com.shouwn.graduation.repository

import com.shouwn.graduation.model.domain.entity.Requirement
import org.springframework.data.neo4j.annotation.Depth
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RequirementRepository : Neo4jRepository<Requirement, Long> {

    @Query("""
        MATCH p = (requirement: Requirement) -[*..]-> ()
        OPTIONAL MATCH p2 = (party:Party) -[:APPLY]-> (requirement)
        RETURN p, p2
    """)
    fun findAllSubs(): Set<Requirement>

    @Query("""
        MATCH (requirement: Requirement)
        WHERE requirement.name =~ {name}
        RETURN requirement
    """)
    fun findAllLikeName(@Param("name") name: String): List<Requirement>
}
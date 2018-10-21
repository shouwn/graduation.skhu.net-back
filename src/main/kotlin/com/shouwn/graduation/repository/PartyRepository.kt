package com.shouwn.graduation.repository

import com.shouwn.graduation.model.domain.entity.Party
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.query.Param

interface PartyRepository : Neo4jRepository<Party, Long> {

    @Query("""
        MATCH (p: Party { name: {name} })
        RETURN COUNT(p) > 0
    """)
    fun existByName(@Param("name") name: String): Boolean

    @Query("""
        WITH {party} AS party
        MATCH (p: Party)
        WHERE id(p) = {partyId}
        SET p.name = party.name, p.belong = party.belong,
          p.updatedAt = party.updatedAt, p.updatedBy = party.updatedBy
        RETURN p
    """)
    fun updateById(@Param("partyId") partyId: Long, party: Party): Party
}
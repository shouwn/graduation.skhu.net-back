package com.shouwn.graduation.repository

import com.shouwn.graduation.model.domain.dto.UserData
import com.shouwn.graduation.model.domain.entity.Party
import com.shouwn.graduation.model.domain.entity.User
import com.sun.org.apache.xpath.internal.XPath.MATCH
import org.springframework.data.domain.Pageable
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : Neo4jRepository<User, Long>{
    fun findByUserNumberOrEmail(userNumber: String, email: String): User?

    @Query("""
        MATCH(u: User)
        WHERE u.userNumber = {userNumber} OR u.email = {email}
        RETURN COUNT(u) > 0
    """)
    fun existsByUserNumberOrEmail(@Param("userNumber") username: String, @Param("email") email: String): Boolean

    fun findAllByEnabled(enabled: Boolean): List<User>

    @Query("""
        MATCH (n)
        WHERE id(n) = {id}
        SET n.enabled = true
    """)
    fun userSetEnable(@Param("id") id: Long)

    @Query("""
        MATCH (u: User)
        WHERE ID(u) = {userId}
        SET u.name = {name}, u.password = {password}, u.email = {email},
          u.hint = {hint}, u.hintAnswer = {hintAnswer}, u.updatedAt = {updatedAt} WITH u
        OPTIONAL MATCH (u) -[r:BELONG]-> ()
        DELETE r WITH u, {parties} AS parties
        UNWIND parties AS party
        MATCH (target: Party)
        WHERE ID(target) = party.id
        CREATE p = (u) -[r:BELONG]-> (target)
        RETURN p
    """)
    fun updateUser(@Param("userId") userId: Long,
                   @Param("name") name: String,
                   @Param("password") password: String,
                   @Param("email") email: String,
                   @Param("hint") hint: Long,
                   @Param("hintAnswer") hintAnswer: String,
                   @Param("updatedAt") updatedAt: String,
                   @Param("parties") parties: Set<Party>): User

    @Query("""
        MATCH (u: User)
        WHERE ID(u) = {userId}
        SET u.password = {password}, u.updatedAt = {updatedAt}
    """)
    fun modifyPassword(@Param("userId") userId: Long,
                       @Param("password") password: String,
                       @Param("updatedAt") updatedAt: String)

    @Query("""
        MATCH (u: User) -[:BELONG]-> (party) WITH u, COLLECT(party) AS parties
        WHERE u.name =~ {name} AND u.userNumber =~ {userNumber} AND {partyId} IN EXTRACT(p IN parties | ID(p))
        optional MATCH (u) -[r:ATTEND]-> () WITH u, parties, SUM(r.credit) AS credit
        WHERE {minCredit} <= credit AND credit <= {maxCredit}
        RETURN u, parties, credit
    """)
    fun findAllBySearch(@Param("name") name: String,
                        @Param("userNumber") userNumber: String,
                        @Param("partyId") partyId: Long,
                        @Param("minCredit") minCredit: Long,
                        @Param("maxCredit") maxCredit: Long,
                        pageable: Pageable): UserData
}
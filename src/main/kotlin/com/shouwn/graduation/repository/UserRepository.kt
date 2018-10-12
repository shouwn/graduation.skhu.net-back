package com.shouwn.graduation.repository

import com.shouwn.graduation.model.domain.entity.User
import com.sun.org.apache.xpath.internal.XPath.MATCH
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
}
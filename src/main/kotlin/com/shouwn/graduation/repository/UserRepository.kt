package com.shouwn.graduation.repository

import com.shouwn.graduation.model.domain.entity.User
import com.sun.org.apache.xpath.internal.XPath.MATCH
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : Neo4jRepository<User, Long>{
    fun findByUsernameOrEmail(username: String, email: String): User?

    @Query("""
                MATCH(u: User)
                WHERE u.username = {username} OR u.email = {email}
                RETURN COUNT(u) > 0
                """)
    fun existsByUsernameOrEmail(@Param("username") username: String, @Param("email") email: String): Boolean
}
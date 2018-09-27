package com.shouwn.graduation.repository

import com.shouwn.graduation.model.domain.entity.User
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : Neo4jRepository<User, Long>{
    fun findByUsername(username: String): User?
}
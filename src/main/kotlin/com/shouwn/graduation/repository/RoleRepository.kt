package com.shouwn.graduation.repository

import com.shouwn.graduation.model.domain.entity.Role
import com.shouwn.graduation.model.domain.type.RoleName
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : Neo4jRepository<Role, Long> {
    fun findByRole(role: RoleName): Role?
}
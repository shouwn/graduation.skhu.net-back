package com.shouwn.graduation.repository

import com.shouwn.graduation.model.domain.entity.Requirement
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Repository
interface RequirementRepository : Neo4jRepository<Requirement, Long>
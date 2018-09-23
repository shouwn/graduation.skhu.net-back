package com.shouwn.graduation.repository

import com.shouwn.graduation.model.entity.Requirement
import org.springframework.data.neo4j.repository.Neo4jRepository

interface RequirementRepository : Neo4jRepository<Requirement, Long>
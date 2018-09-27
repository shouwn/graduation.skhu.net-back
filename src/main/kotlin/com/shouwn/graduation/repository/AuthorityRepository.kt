package com.shouwn.graduation.repository

import com.shouwn.graduation.model.domain.entity.Authority
import org.springframework.data.neo4j.repository.Neo4jRepository

interface AuthorityRepository : Neo4jRepository<Authority, Long>
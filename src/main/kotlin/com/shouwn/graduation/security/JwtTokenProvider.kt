package com.shouwn.graduation.security

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtTokenProvider constructor(
        @Value("\${app.jwtSecret}")
        private val jwtSecret: String,

        @Value("\${app.jwtExpirationMs}")
        private val jwtExpirationMs: Int
){
    private val logger = LoggerFactory.getLogger(JwtTokenProvider::class.java)
    private val secretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

    fun generateToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserPrincipal

        return generateToken(userPrincipal.id.toString())
    }

    fun generateToken(id: String): String {

        val now = Date()

        val expiryDateTime = Date(now.time + jwtExpirationMs)

        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(now)
                .setExpiration(expiryDateTime)
                .signWith(secretKey)
                .compact()
    }

    fun getUserIdFromJWT(token: String): Long{
        val claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .body

        return claims.subject.toLong()
    }

    fun validateToken(authToken: String?): Boolean {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken)
            return true
        } catch (ex: SecurityException) {
            logger.error("Invalid JWT signature")
        } catch (ex: MalformedJwtException) {
            logger.error("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            logger.error("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            logger.error("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            logger.error("JWT claims string is empty.")
        }

        return false
    }
}
package com.jetbrains.handson.httpapi.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.jetbrains.handson.httpapi.model.AccountId
import com.jetbrains.handson.httpapi.model.AuthToken
import io.ktor.auth.Principal
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Clock
import java.util.*

class AuthenticationTokenService(
    publicKey: RSAPublicKey,
    privateKey: RSAPrivateKey,
    private val clock: Clock
) {
    private val jwtAlgorithm = Algorithm.RSA256(publicKey, privateKey)
    val jwtVerifier =
        (JWT.require(jwtAlgorithm) as JWTVerifier.BaseVerification)
            .build { Date(clock.millis()) }

    fun generateAuthToken(accountId: AccountId): AuthToken {
        try {
            return JWT.create()
                .withIssuer("kiq83")
                .withSubject(accountId.toString())
                .withExpiresAt(Date.from(clock.instant().plusSeconds(3600L)))
                .sign(jwtAlgorithm)
        } catch (e: JWTCreationException) {
            //Invalid Signing configuration / Couldn't convert Claims.
            throw e
        }
    }
}

data class CustomPrincipal(val accountId: AccountId) : Principal
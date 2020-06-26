package com.jetbrains.handson.httpapi.configuration

import com.jetbrains.handson.httpapi.repository.inMemory.AccountRepositoryInMemory
import com.jetbrains.handson.httpapi.security.AuthenticationService
import com.jetbrains.handson.httpapi.security.AuthenticationTokenService
import com.typesafe.config.ConfigFactory
import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.time.Clock

object BaseConfiguration {
    val clock = Clock.systemUTC()!!

    private val rootConfig = ConfigFactory.load()

    private val serverConfig = rootConfig.getConfig("ktor-server")
    val httpPort = serverConfig.getInt("port")

    val authenticationTokenService = AuthenticationTokenService(
        retrieveRSAPublicKey(),
        retrieveRSAPrivateKey(),
        clock
    )

    val authenticationService = AuthenticationService(authenticationTokenService)

    val accountRepository = AccountRepositoryInMemory()
}

private fun retrieveRSAPublicKey(): RSAPublicKey {
    val keyBytes = Files.readAllBytes(
        Paths.get("src/main/resources/public_key.der")
    )

    val spec = X509EncodedKeySpec(keyBytes)
    val kf = KeyFactory.getInstance("RSA")
    return kf.generatePublic(spec) as RSAPublicKey
}

private fun retrieveRSAPrivateKey(): RSAPrivateKey {
    val keyBytes = Files.readAllBytes(
        Paths.get("src/main/resources/private_key.der")
    )

    val spec = PKCS8EncodedKeySpec(keyBytes)
    val kf = KeyFactory.getInstance("RSA")
    return kf.generatePrivate(spec) as RSAPrivateKey
}
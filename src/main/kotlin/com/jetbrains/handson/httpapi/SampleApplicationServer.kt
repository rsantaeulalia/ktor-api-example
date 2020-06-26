package com.jetbrains.handson.httpapi

import com.jetbrains.handson.httpapi.repository.AccountRepository
import com.jetbrains.handson.httpapi.routes.*
import com.jetbrains.handson.httpapi.security.AuthenticationService
import com.jetbrains.handson.httpapi.security.AuthenticationTokenService
import com.jetbrains.handson.httpapi.security.CustomPrincipal
import io.ktor.application.ApplicationCall
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.jwt.jwt
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.util.*


class SampleApplicationServer(
    private val httpPort: Int,
    private val tokenService: AuthenticationTokenService,
    private val authenticationService: AuthenticationService,
    private val accountRepository: AccountRepository
) {
    fun start() {
        embeddedServer(Netty, httpPort) {

            install(DefaultHeaders)
            install(ContentNegotiation) {
                gson()
            }
            install(Authentication) {
                jwt {
                    realm = "sample ktor"
                    verifier(tokenService.jwtVerifier)
                    validate { credential ->
                        CustomPrincipal(UUID.fromString(credential.payload.subject))
                    }
                }
            }

            routing {
                route("api") {
                    auth(authenticationService)
                    registerHealthRoutes()
                    authenticate {
                        account(accountRepository)
                        order()
                        registerCustomerRoutes()
                    }
                }
            }
        }.start(wait = true)
    }
}

fun ApplicationCall.getLoggedAccountId() =
    authentication.principal<CustomPrincipal>()?.accountId!!


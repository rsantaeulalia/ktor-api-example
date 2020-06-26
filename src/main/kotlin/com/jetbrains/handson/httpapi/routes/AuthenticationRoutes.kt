package com.jetbrains.handson.httpapi.routes

import com.jetbrains.handson.httpapi.model.AccountId
import com.jetbrains.handson.httpapi.security.AuthenticationService
import com.jetbrains.handson.httpapi.security.CredentialAlreadyExistsException
import com.jetbrains.handson.httpapi.security.InvalidCredentialsException
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.auth(authenticationService: AuthenticationService) {

    route("auth") {

        post("sign-up") {
            val credentials = call.receive(Credentials::class)
            try {
                val accountId = authenticationService.signUp(credentials.email, credentials.password)

                call.respond(HttpStatusCode.OK, AuthResponse(accountId))
            } catch (e: CredentialAlreadyExistsException) {
                call.respond(HttpStatusCode.BadRequest, e.message)
            }
        }

        post("sign-in") {
            val credentials = call.receive(Credentials::class)

            try {
                val response = authenticationService.signIn(credentials.email, credentials.password)
                call.response.header("x-auth-new-token", response.newToken)
                call.respond(HttpStatusCode.OK, AuthResponse(response.accountId))
            } catch (e: InvalidCredentialsException) {
                call.respond(HttpStatusCode.BadRequest, e.message)
            }

        }
    }
}

data class Credentials(val email: String, val password: String) {
    override fun toString(): String {
        // doing this so we never risk logging the password
        return "Credentials(email=$email, password=****"
    }
}

data class AuthResponse(val accountId: AccountId)
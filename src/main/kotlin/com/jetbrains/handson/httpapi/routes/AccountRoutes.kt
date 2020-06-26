package com.jetbrains.handson.httpapi.routes

import com.jetbrains.handson.httpapi.getLoggedAccountId
import com.jetbrains.handson.httpapi.model.AccountProfile
import com.jetbrains.handson.httpapi.repository.AccountRepository
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route
import java.util.*

fun Route.account(accountProfileDAO: AccountRepository) {
    route("account/{accountId}") {

        route("profile") {

            get {
                val requestAccountId = UUID.fromString(call.parameters["accountId"]!!)
                val loggedAccountId = call.getLoggedAccountId()
                if (requestAccountId != loggedAccountId) {
                    call.respond(HttpStatusCode.Forbidden, "")
                } else {
                    val profile = accountProfileDAO.getAccountProfile(requestAccountId)
                    if (profile != null) {
                        call.respond(HttpStatusCode.OK, profile)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Could not find profile for sent id")
                    }
                }
            }

            put {
                val requestAccountId = UUID.fromString(call.parameters["accountId"]!!)
                val loggedAccountId = call.getLoggedAccountId()
                if (requestAccountId != loggedAccountId) {
                    call.respond(HttpStatusCode.Forbidden, "")
                } else {

                    val profile = call.receive(AccountProfile::class)
                    accountProfileDAO.putAccountProfile(requestAccountId, profile)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}
package com.jetbrains.handson.httpapi.routes

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.routing

fun Application.registerHealthRoutes() {
    routing {
        healtCheckRoute()
    }
}

fun Route.healtCheckRoute() {
    get("/health") {
        call.respondText("I'm a healthy server")
    }
}
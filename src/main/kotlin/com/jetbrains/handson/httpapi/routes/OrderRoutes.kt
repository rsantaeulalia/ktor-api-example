package com.jetbrains.handson.httpapi.routes

import com.jetbrains.handson.httpapi.model.orderStorage
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route

fun Route.order() {
    route("orders") {
        get {
            if (orderStorage.isNotEmpty()) {
                call.respond(orderStorage)
            }
        }

        route("/{id}") {
            get {
                val id = call.parameters["id"] ?: return@get call.respondText(
                    "Bad Request",
                    status = HttpStatusCode.BadRequest
                )
                val order = orderStorage.find { it.number == id } ?: return@get call.respondText(
                    "Not Found",
                    status = HttpStatusCode.NotFound
                )
                call.respond(order)
            }

            get("/total") {
                val id = call.parameters["id"] ?: return@get call.respondText(
                    "Bad Request",
                    status = HttpStatusCode.BadRequest
                )
                val order = orderStorage.find { it.number == id } ?: return@get call.respondText(
                    "Not Found",
                    status = HttpStatusCode.NotFound
                )
                val total = order.contents.map { it.price * it.amount }.sum()
                call.respond(total)
            }
        }
    }
}
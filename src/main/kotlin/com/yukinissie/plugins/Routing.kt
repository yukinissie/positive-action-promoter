package com.yukinissie.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/") {
            get {
                call.respondText("Hi, this is yukinissie!", ContentType.Text.Html)
            }

            // return received text * 3
            post("/") {
                val text = call.receiveText()
                call.respondText("$text-$text-$text")
            }
        }
    }
}

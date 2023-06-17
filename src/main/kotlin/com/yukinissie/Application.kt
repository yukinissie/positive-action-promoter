package com.yukinissie

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.yukinissie.plugins.configureRouting
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.testing.*


public class App {
    public fun handleRequest(event: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {
        val responseEvent = APIGatewayProxyResponseEvent()

        withTestApplication({
            module()
        }) {
            handleRequest(HttpMethod(event.httpMethod), "/") {
                if (event.body != null) setBody(event.body)
            }.apply {
                responseEvent.statusCode = response.status()?.value
                responseEvent.body = response.content
            }
        }

        return responseEvent
    }
}

fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    configureRouting()
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

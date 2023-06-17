package com.yukinissie

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk


class ApplicationTest : FunSpec({

    context("handleRequest") {
        val app = App()
        val event = APIGatewayProxyRequestEvent()
        val context = mockk<Context>()

        test("GET") {
            event.httpMethod = "GET"
            val response = app.handleRequest(event, context)

            response.statusCode shouldBe 200
            response.body shouldBe "Hi, this is yukinissie!"
        }

        test("POST") {
            event.httpMethod = "POST"
            event.body = "test"
            val response = app.handleRequest(event, context)

            response.statusCode shouldBe 200
            response.body shouldBe "test-test-test"
        }
    }
})

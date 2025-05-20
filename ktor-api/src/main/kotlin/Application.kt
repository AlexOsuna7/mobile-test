package com.example

import com.example.model.SeedResponse
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Serializable
data class SeedResponse(val seed: String,val expiresAt: Long)
@Serializable
data class ValidationResponse(val valid: Boolean, val reason: String? = null)

val seedStorage = ConcurrentHashMap<String, Instant>() // seed -> expiration time

fun main() {
    embeddedServer(Netty, host = "0.0.0.0", port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/seed") {
            val seed = UUID.randomUUID().toString().replace("-", "")
            val expiration = Instant.now().plus(1, ChronoUnit.MINUTES)

            seedStorage[seed] = expiration

            // 🔵 Log: new seed generated
            println("🔵 New seed generated: $seed")
            println("📦 Current stored seeds:")
            seedStorage.forEach { (key, value) ->
                println(" - $key -> Expires at: $value")
            }

            call.respond(SeedResponse(seed, expiration.toEpochMilli()))
        }

        get("/validate") {
            val seed = call.request.queryParameters["seed"]

            // 🟡 Log: seed being validated
            println("🟡 Validating seed: $seed")

            if (seed.isNullOrBlank()) {
                println("⚠️ Missing seed in request")
                call.respond(HttpStatusCode.BadRequest, ValidationResponse(false, "Missing seed"))
                return@get
            }

            val expiration = seedStorage[seed]
            if (expiration == null) {
                println("❌ Seed not found: $seed")
                call.respond(HttpStatusCode.NotFound, ValidationResponse(false, "Seed not found"))
            } else if (Instant.now().isAfter(expiration)) {
                println("⚠️ Seed expired: $seed")
                call.respond(HttpStatusCode.Gone, ValidationResponse(false, "Seed expired"))
            } else {
                println("✅ Seed is valid: $seed")
                call.respond(ValidationResponse(true))
            }
        }
    }
}
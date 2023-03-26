package natiqhaciyef.com

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import kotlinx.serialization.Serializable
import natiqhaciyef.com.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", watchPaths = listOf("classes","resources"),
        module = Application::myApplicationModule).start(wait = true)
}

fun Application.module() {
    configureRouting()
}

fun Application.myApplicationModule() {
    install(ContentNegotiation) {
        json()
    }
//    module()
//    module2()
    module3()
}


fun Application.module2() {
    routing {

        // testing first request and respond
        get("/data") {
            call.respondText("Data success")
        }

        // path parameter
        get("/data/{username}") {
            val username = call.parameters["username"]

            if (username == "admin") {
                call.response.header(name = "CustomHeader", "AdminCaase")
                call.respond(message = "Hello Admin", status = HttpStatusCode.OK)
            } else {
                call.respondText("Greeting $username")
            }
        }

        // query parameters
        get("/user") {
            val name = call.request.queryParameters["name"]
            val age = call.request.queryParameters["age"]

            call.respondText("Hello $name, you are $age years old...")
        }

        // json usage
        get("/person") {
            try {
                val person = Person(name = "Natiq", age = 23)
                call.respond(message = person, status = HttpStatusCode.OK)
            } catch (e: Exception) {
                call.respond(message = "Something went wrong", status = HttpStatusCode.BadRequest)
            }
        }

        // redirect
        get("/redirect") {
            call.respondRedirect(url = "/person", permanent = false)
        }
    }
}

fun Application.module3() {
    routing {
        static(remotePath = "data") {
            // remote path using for access data in resources
            // example : localhost:8080/data/kotlin.html
            resources("static")
            resource("kotlin.html")
        }

        // kotlin dsl usage
        get("/welcome") {
            val name = call.request.queryParameters["name"]
            call.respondHtml {
                head {
                    title { +"Welcome back" }
                }
                body {
                    if (name.isNullOrEmpty())
                        h3 { + "Welcome!"}
                    else
                        h3 { +"Welcome, $name" }

                    img(src = "https://mathiasfrohlich.gallerycdn.vsassets.io/extensions/mathiasfrohlich/kotlin/1.7.1/1581441165235/Microsoft.VisualStudio.Services.Icons.Default")
                }
            }
        }

        get("/information"){
            call.respondText("Information page")
        }
    }
}


@Serializable
data class Person(val name: String, val age: Int)
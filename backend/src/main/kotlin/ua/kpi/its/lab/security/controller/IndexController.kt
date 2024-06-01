package ua.kpi.its.lab.security.controller

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller for handling the root URL. Provides a simple method to demonstrate
 * authentication-based greetings.
 */
@RestController
class IndexController {

    /**
     * Responds to GET requests at the root URL by greeting the authenticated user.
     *
     * @param authentication The authentication object representing the current user.
     * @return A greeting string that includes the authenticated user's name.
     */
    @GetMapping("/")
    fun hello(authentication: Authentication) = "Hello, ${authentication.name}!"
}

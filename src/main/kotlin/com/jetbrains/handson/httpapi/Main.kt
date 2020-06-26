package com.jetbrains.handson.httpapi

import com.jetbrains.handson.httpapi.configuration.BaseConfiguration.accountRepository
import com.jetbrains.handson.httpapi.configuration.BaseConfiguration.authenticationService
import com.jetbrains.handson.httpapi.configuration.BaseConfiguration.authenticationTokenService
import com.jetbrains.handson.httpapi.configuration.BaseConfiguration.httpPort


fun main(args: Array<String>) {
    println("Initiating Account Server")

    SampleApplicationServer(
        httpPort = httpPort,
        tokenService = authenticationTokenService,
        authenticationService = authenticationService,
        accountRepository = accountRepository
    ).start()
}

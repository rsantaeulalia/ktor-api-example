package com.jetbrains.handson.httpapi.model

import java.util.*

typealias AccountId = UUID
typealias AuthToken = String

data class AccountProfile(
    val firstName: String,
    val lastName: String
)
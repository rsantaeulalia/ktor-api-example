package com.jetbrains.handson.httpapi.security

import com.jetbrains.handson.httpapi.model.AccountId
import com.jetbrains.handson.httpapi.model.AuthToken
import java.util.*

class AuthenticationService(
    private val authenticationTokenService: AuthenticationTokenService
) {

    private val credentialsMap = mutableMapOf<String, AuthCredentialInfo>()

    fun signUp(email: String, password: String): AccountId {
        if (credentialsMap[email] != null) {
            throw CredentialAlreadyExistsException("Credential already exists for email=$email")
        } else {
            val accountId = UUID.randomUUID()
            credentialsMap[email] = AuthCredentialInfo(password, accountId)
            return accountId
        }
    }

    fun signIn(email: String, password: String): SignInResponse {
        val credentialInfo = credentialsMap[email]
        if (credentialInfo == null || credentialInfo.password != password) {
            throw InvalidCredentialsException(
                "Invalid credentials sent. " +
                        "Either there is no account for email = $email or the sent password is wrong"
            )
        }
        // valid login
        val newToken = authenticationTokenService.generateAuthToken(credentialInfo.accountId)
        return SignInResponse(credentialInfo.accountId, newToken)
    }
}

data class AuthCredentialInfo(val password: String, val accountId: AccountId)
data class SignInResponse(val accountId: AccountId, val newToken: AuthToken)

class CredentialAlreadyExistsException(override val message: String) : Exception(message)
class InvalidCredentialsException(override val message: String) : Exception(message)
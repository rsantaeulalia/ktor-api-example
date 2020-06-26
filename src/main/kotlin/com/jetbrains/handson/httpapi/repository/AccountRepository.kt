package com.jetbrains.handson.httpapi.repository

import com.jetbrains.handson.httpapi.model.AccountId
import com.jetbrains.handson.httpapi.model.AccountProfile

interface AccountRepository {
    fun putAccountProfile(accountId: AccountId, accountProfile: AccountProfile)
    fun getAccountProfile(accountId: AccountId): AccountProfile?
}